package com.example;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.FileReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.specific.SpecificDatumReader;

/**
 * Test reading a no namespace avro data file with a namespaced reader schema with field aliases.
 */

public class Main {

    /**
     * Write a file with a no namespace schema then read it with a namespaced schema
     * 
     * @param args command line arguments
     */
    public static void main( String[] args ) 
        throws IOException
    {
        File file = new File("/tmp/record.avro");
        writeFile(file);
        readFile(file);
    }
    
    static void readFile(File file)
        throws IOException
    {
        DatumReader<MyRecord> datumReader = new SpecificDatumReader<>(MyRecord.class);
        FileReader<MyRecord> fileReader = DataFileReader.openReader(file, datumReader);
        
        MyRecord record = null;
        while (fileReader.hasNext()) {
            record = fileReader.next(record);
            CharSequence name = record.getName();
            Double temp = record.getTemperatureC();
            System.out.println(name + " " + temp);
        }
    }

    static void writeFile(File file)
        throws IOException
    {
        InputStream stream = Main.class.getResourceAsStream("/writer.avsc");
        Schema schema = new Schema.Parser().parse(stream);
        stream.close();

        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        DataFileWriter<GenericRecord> fileWriter = new DataFileWriter<>(datumWriter);
        fileWriter.create(schema, file);

        GenericRecord record = new GenericData.Record(schema);
        record.put("Name", "Fred");
        record.put("Temperature", 27.2);
        fileWriter.append(record);

        fileWriter.close();
    }
}

package com.example;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.specific.SpecificDatumReader;

/**
 * Test reading a no namespace avro data file with a union containing a record type.
 */

public class Main {

    public static void main( String[] args ) 
        throws IOException
    {
        File file = new File("unionrecord.avro");
        readFile(file);
    }

    static void readFile(File file)
        throws IOException
    {
        InputStream stream = Main.class.getResourceAsStream("/unionwriter.avsc");
        Schema schema = new Schema.Parser().parse(stream);
        stream.close();

        SpecificDatumReader<TestUnionNested> datumReader = new SpecificDatumReader<>(schema);

       // System.out.println("actual:   " + datumReader.getSchema());
       // System.out.println("expected: " + datumReader.getExpected());

        DataFileReader<TestUnionNested> fileReader = new DataFileReader<>(file, datumReader);
        TestUnionNested record = fileReader.next();

        System.out.println(record);
  
        fileReader.close();
    }
}

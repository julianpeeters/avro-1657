package com.example;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.specific.SpecificDatumWriter;

/**
 * Test writing a no namespace avro data file with a union containing a record type.
 */

public class Main {

    public static void main( String[] args ) 
        throws IOException
    {
        File file = new File("/tmp/unionrecord.avro");
        writeFile(file);
    }

    static void writeFile(File file)
        throws IOException
    {
        InputStream stream = Main.class.getResourceAsStream("/unionwriter.avsc");
        Schema schema = new Schema.Parser().parse(stream);
        stream.close();

        Test test = new Test(1);
        TestUnionNested testNested = new TestUnionNested(test);

        SpecificDatumWriter<TestUnionNested> datumWriter = new SpecificDatumWriter<>(schema);
        DataFileWriter<TestUnionNested> fileWriter = new DataFileWriter<>(datumWriter);

        fileWriter.create(schema, file);
        fileWriter.append(testNested);
        fileWriter.close();
    }
}

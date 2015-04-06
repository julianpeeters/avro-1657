## Issues reading namespace-less schemas from namespaced Specific Records.

Reading a union of specific records from a datafile fails when the reader's schema has a namespace and the writer's schema is without. 




  #### Providing the DatumReader with a namespaced schema:
    

Datum reader stumbles trying to read a record instance because the writer schema and reader schema do not match.


    package com.example

    ...

    SpecificDatumReader<TestUnionNested> datumReader = new SpecificDatumReader<>(TestUnionNested.class);
    DataFileReader<TestUnionNested> fileReader = new DataFileReader<>(file, datumReader);
    TestUnionNested record = fileReader.next();

Results in:


    Exception in thread "main" org.apache.avro.AvroTypeException: Found Test, expecting union
	at org.apache.avro.io.ResolvingDecoder.doAction(ResolvingDecoder.java:292)
	at org.apache.avro.io.parsing.Parser.advance(Parser.java:88)
	at org.apache.avro.io.ResolvingDecoder.readIndex(ResolvingDecoder.java:267)
	at org.apache.avro.generic.GenericDatumReader.read(GenericDatumReader.java:155)
	at org.apache.avro.generic.GenericDatumReader.readField(GenericDatumReader.java:193)
	at org.apache.avro.generic.GenericDatumReader.readRecord(GenericDatumReader.java:183)
	at org.apache.avro.generic.GenericDatumReader.read(GenericDatumReader.java:151)
	at org.apache.avro.generic.GenericDatumReader.read(GenericDatumReader.java:142)
	at org.apache.avro.file.DataFileStream.next(DataFileStream.java:233)
	at org.apache.avro.file.DataFileStream.next(DataFileStream.java:220)
	at com.example.Main.readFile(Main.java:37)
	at com.example.Main.main(Main.java:21)


  #### Providing the DatumReader with a schema without a namespace: 

Now the schema resolves when the datum writer attempts to read in data, but without a namespace, the full name of the record no longer matches the specific class name, and so it punts to generic.

    InputStream stream = Main.class.getResourceAsStream("/unionwriter.avsc");
    Schema schema = new Schema.Parser().parse(stream);
    stream.close();

    SpecificDatumReader<TestUnionNested> datumReader = new SpecificDatumReader<>(schema);
    DataFileReader<TestUnionNested> fileReader = new DataFileReader<>(file, datumReader);
    TestUnionNested record = fileReader.next();


Results in:


    Exception in thread "main" java.lang.ClassCastException: org.apache.avro.generic.GenericData$Record cannot be cast to com.example.TestUnionNested
	at com.example.Main.readFile(Main.java:37)
	at com.example.Main.main(Main.java:21)

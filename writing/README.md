## Issues writing namespace-less schemas from namespaced Specific Records.

Writing a union of specific records to a datafile fails when class's schema has a namespace and the writer's schema does not. When trying to resolve the union, the full name of the namespaceless record does not match the full name of the specific class.


    package com.example

    ...

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


Results in:


    Exception in thread "main" org.apache.avro.file.DataFileWriter$AppendWriteException: org.apache.avro.UnresolvedUnionException: Not in union ["null",{"type":"record","name":"Test","fields":[{"name":"x","type":"int"}]}]: {"x": 1}
	at org.apache.avro.file.DataFileWriter.append(DataFileWriter.java:296)
	at com.example.Main.writeFile(Main.java:38)
	at com.example.Main.main(Main.java:21)
    Caused by: org.apache.avro.UnresolvedUnionException: Not in union ["null",{"type":"record","name":"Test","fields":[{"name":"x","type":"int"}]}]: {"x": 1}
	at org.apache.avro.generic.GenericData.resolveUnion(GenericData.java:604)
	at org.apache.avro.generic.GenericDatumWriter.resolveUnion(GenericDatumWriter.java:151)
	at org.apache.avro.generic.GenericDatumWriter.write(GenericDatumWriter.java:71)
	at org.apache.avro.generic.GenericDatumWriter.writeField(GenericDatumWriter.java:114)
	at org.apache.avro.generic.GenericDatumWriter.writeRecord(GenericDatumWriter.java:104)
	at org.apache.avro.generic.GenericDatumWriter.write(GenericDatumWriter.java:66)
	at org.apache.avro.generic.GenericDatumWriter.write(GenericDatumWriter.java:58)
	at org.apache.avro.file.DataFileWriter.append(DataFileWriter.java:290)
	... 2 more


Aliasing the class name doesn't seem to succeed as a workaround (the same error as without aliases).
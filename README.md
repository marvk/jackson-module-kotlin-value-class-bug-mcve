# MCVE for jackson-kotlin-module value class deserialization bug

There exists a bug in jackson-kotlin-module. When trying to deserialize a CSV into a target class that contains a field that is a `value class` and
the `ObjectMapper` has a `CsvSchema` that has column reordering enabled, jackson will produce the following exception:

    Exception in thread "main" com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException: Unrecognized field "BAR_HEADER" (class net.marvk.FooWithValueClass), not marked as ignorable (2 known properties: "bar", "baz"])
    at [Source: (StringReader); line: 2, column: 41] (through reference chain: net.marvk.FooWithValueClass["BAR_HEADER"])
    at com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException.from(UnrecognizedPropertyException.java:61)
    at com.fasterxml.jackson.databind.DeserializationContext.handleUnknownProperty(DeserializationContext.java:1153)
    at com.fasterxml.jackson.databind.deser.std.StdDeserializer.handleUnknownProperty(StdDeserializer.java:2241)
    at com.fasterxml.jackson.databind.deser.BeanDeserializerBase.handleUnknownProperty(BeanDeserializerBase.java:1793)
    at com.fasterxml.jackson.databind.deser.BeanDeserializerBase.handleUnknownProperties(BeanDeserializerBase.java:1743)
    at com.fasterxml.jackson.databind.deser.BeanDeserializer._deserializeUsingPropertyBased(BeanDeserializer.java:546)
    at com.fasterxml.jackson.databind.deser.BeanDeserializerBase.deserializeFromObjectUsingNonDefault(BeanDeserializerBase.java:1493)
    at com.fasterxml.jackson.databind.deser.BeanDeserializer.deserializeFromObject(BeanDeserializer.java:348)
    at com.fasterxml.jackson.databind.deser.BeanDeserializer.deserialize(BeanDeserializer.java:185)
    at com.fasterxml.jackson.databind.MappingIterator.nextValue(MappingIterator.java:283)
    at com.fasterxml.jackson.databind.MappingIterator.readAll(MappingIterator.java:323)
    at com.fasterxml.jackson.databind.MappingIterator.readAll(MappingIterator.java:309)
    at net.marvk.MainKt.main(Main.kt:20)
    at net.marvk.MainKt.main(Main.kt)

This issue does not exist for data classes or if column reordering is disabled.

|                           | `data class` | `value class` |
|---------------------------|--------------|---------------|
| Column Reordering `false` | ✔            | ✔             |
| Column Reordering `true`  | ✔            | ❌             |

See `src/main/kotlin/Main.kt` for the code and `src/test/kotlin/Test.kt` for tests which demonstrate that data classes work whether column reordering is enabled
or not, and value classes work with column reordering disabled.

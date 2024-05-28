import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvParser
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.module.kotlin.kotlinModule

/**
 * Just show me the bug!
 */
fun main() {
    createReader<FooWithValueClass>(true)
        .readValues<FooWithValueClass>(CSV)
        .readAll()
}

val CSV = """
    BAR_HEADER,BAZ_HEADER
    bar1,baz1
""".trimIndent()

val MAPPER =
    CsvMapper
        .builder()
        .addModule(
            SimpleModule()
                .addDeserializer(BazValueClass::class.java, object : JsonDeserializer<BazValueClass>() {
                    override fun deserialize(p: JsonParser, ctxt: DeserializationContext) = BazValueClass(p.valueAsString)
                })
                .addDeserializer(BazDataClass::class.java, object : JsonDeserializer<BazDataClass>() {
                    override fun deserialize(p: JsonParser, ctxt: DeserializationContext) = BazDataClass(p.valueAsString)
                })
        )
        .addModule(kotlinModule())
        .build()

inline fun <reified T> createSchema(columnReordering: Boolean): CsvSchema =
    MAPPER
        .schemaFor(T::class.java)
        .withColumnReordering(columnReordering)
        .withHeader()

inline fun <reified T> createReader(columnReordering: Boolean): ObjectReader =
    MAPPER
        .readerFor(T::class.java)
        .with(CsvParser.Feature.WRAP_AS_ARRAY)
        .with(createSchema<T>(columnReordering))

data class FooWithValueClass(
    @JsonProperty("BAR_HEADER")
    val bar: String?,
    @JsonProperty("BAZ_HEADER")
    val baz: BazValueClass?,
)

data class FooWithDataClass(
    @JsonProperty("BAR_HEADER")
    val bar: String?,
    @JsonProperty("BAZ_HEADER")
    val baz: BazDataClass?,
)

@JvmInline
value class BazValueClass(val value: String)

data class BazDataClass(val value: String)

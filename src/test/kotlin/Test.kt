import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.collections.shouldContainExactly
import net.marvk.BazDataClass
import net.marvk.BazValueClass
import net.marvk.CSV
import net.marvk.FooWithDataClass
import net.marvk.FooWithValueClass
import net.marvk.createReader
import org.junit.jupiter.api.Test

class Test {
    @Test
    fun valueClassWithoutColumnReordering() {
        val actual = shouldNotThrowAny {
            createReader<FooWithValueClass>(false)
                .readValues<FooWithValueClass>(CSV)
                .readAll()
        }

        actual shouldContainExactly listOf(FooWithValueClass("bar1", BazValueClass("baz1")))
    }

    /**
     * This one is broken!
     */
    @Test
    fun valueClassWithColumnReordering() {
        val actual = shouldNotThrowAny {
            createReader<FooWithValueClass>(true)
                .readValues<FooWithValueClass>(CSV)
                .readAll()
        }

        actual shouldContainExactly listOf(FooWithValueClass("bar1", BazValueClass("baz1")))
    }

    @Test
    fun dataClassWithoutColumnReordering() {
        val actual = shouldNotThrowAny {
            createReader<FooWithDataClass>(false)
                .readValues<FooWithDataClass>(CSV)
                .readAll()
        }

        actual shouldContainExactly listOf(FooWithDataClass("bar1", BazDataClass("baz1")))
    }

    @Test
    fun dataClassWithColumnReordering() {
        val actual = shouldNotThrowAny {
            createReader<FooWithDataClass>(true)
                .readValues<FooWithDataClass>(CSV)
                .readAll()
        }

        actual shouldContainExactly listOf(FooWithDataClass("bar1", BazDataClass("baz1")))
    }
}

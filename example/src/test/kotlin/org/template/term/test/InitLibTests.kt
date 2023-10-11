package org.template.term.test

import com.klee0kai.example.engine.SimpleJniEngine
import com.klee0kai.example.model.NullableTypePojo
import com.klee0kai.example.model.Simple
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InitLibTests {

    @Test
    fun initTest() {
        //when
        val initResult = SimpleJniEngine.initLib()

        assertEquals(0, initResult, "Init should return 0, if init is correct")
    }

    @Test
    fun deinitTest() {
        //Given
        SimpleJniEngine.initLib()

        //when
        val initResult = SimpleJniEngine.deinitLib()

        //then
        assertEquals(0, initResult, "Init should return 0, if deinit is correct")
    }

    @Test
    fun testCopySimple() {
        SimpleJniEngine.initLib()

        val simple = Simple(2, "name", "address")
        val simple2 = SimpleJniEngine.copySimple(simple)

        assertEquals(3, simple2.age)
        assertEquals("namefrom c++", simple2.name)
        assertEquals("addressfrom c++", simple2.address)
    }


    @Test
    fun testCopyNullableType() {
        // given
        SimpleJniEngine.initLib()

        // when
        val obj1 = NullableTypePojo(
            booleanField = true,
            charField = 'a',
            byteField = 1,
            shortField = 2,
            intField = 3,
            longField = 4,
            floatField = 5.2f,
            doubleField = 6.3,
            stringField = "str",
        )
        val obj2 = SimpleJniEngine.copyNullableType(obj1)

        //then
        assertEquals(true, obj2.booleanField)
        assertEquals('a', obj2.charField)
        assertEquals(1, obj2.byteField)
        assertEquals(2, obj2.shortField)
        assertEquals(3, obj2.intField)
        assertEquals(4, obj2.longField)
        assertEquals(5.2f, obj2.floatField)
        assertEquals(6.3, obj2.doubleField)
        assertEquals("str", obj2.stringField)
    }

}
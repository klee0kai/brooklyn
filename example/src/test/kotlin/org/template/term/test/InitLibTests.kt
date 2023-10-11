package org.template.term.test

import com.klee0kai.example.engine.SimpleJniEngine
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

}
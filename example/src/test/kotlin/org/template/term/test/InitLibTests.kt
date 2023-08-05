package org.template.term.test

import com.klee0kai.example.engine.SimpleJniEngine
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InitLibTests {

    @Test
    fun initTest() {
        //when
        val initResult = SimpleJniEngine.initLib()

        assertEquals(1, initResult)
    }

}
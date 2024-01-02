package com.klee0kai.tests.engine

import com.klee0kai.tests.mirrors.JniCounter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

object KotlinObjectTest {

    @Test
    fun setTo102Test() {
        //when
        SimpleJniEngine.setCounterTo102()

        //then
        assertEquals(102, JniCounter.count)
    }


    @Test
    fun setTo102AndIncrementTest() {
        //when
        SimpleJniEngine.setCounterTo102()
        SimpleJniEngine.counterIncrement()

        //then
        assertEquals(103, JniCounter.count)
    }

}
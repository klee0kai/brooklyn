package org.template.term.test

import com.klee0kai.example.engine.SimpleJniEngine
import com.klee0kai.example.model.Simple
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class ArrayTexts {

    @Test
    fun simpleArrayCopyTest() {
        val array1 = arrayOf(
            Simple(3, "add", "name"),
            Simple(2, "add1", "name6"),
            Simple(5, "add3", "name9"),
            )

        val array2 = SimpleJniEngine.copyArray(array1)

        assertFalse(array1 === array2)
        assertEquals(3, array2.size)
        assertEquals(array1[0], array2[0])
        assertEquals(array1[1], array2[1])
        assertEquals(array1[2], array2[2])
    }

}
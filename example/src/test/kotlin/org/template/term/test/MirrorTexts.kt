package org.template.term.test

import com.klee0kai.example.engine.SimpleJniEngine
import com.klee0kai.example.mirrors.SimpleJniMirror
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MirrorTexts {

    @Test
    fun simpleMirrorIncFromCpp() {
        SimpleJniEngine

        val simple = SimpleJniMirror(1)
        simple.incInCpp()

        assertEquals(2, simple.someInt)
    }

    @Test
    fun simpleMirrorInc2FromCpp() {
        SimpleJniEngine

        val simple = SimpleJniMirror(0)
        val result = simple.incInCpp2()

        assertEquals(1, result)
        assertEquals(2, simple.someInt)
        assertEquals("from C++", simple.someString)
    }
}
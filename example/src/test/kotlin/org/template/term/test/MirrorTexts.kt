package org.template.term.test

import com.klee0kai.example.engine.SimpleJniEngine
import com.klee0kai.example.mirrors.SimpleJniMirror
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class MirrorTexts {

    init {
        SimpleJniEngine
    }

    @Test
    fun simpleMirrorIncFromCpp() {
        val simple = SimpleJniMirror(1)
        simple.incInCpp()

        assertEquals(2, simple.someInt)
    }

    @Test
    fun simpleMirrorInc2FromCpp() {
        val simple = SimpleJniMirror(0)
        val result = simple.incInCpp2()

        assertEquals(1, result)
        assertEquals(2, simple.someInt)
        assertEquals("from C++", simple.someString)
    }


    @Test
    fun updateInCppTest() {
        val simple = SimpleJniMirror(0)
        simple.update(3, "c++")

        assertEquals(3, simple.someInt)
        assertEquals("c++", simple.someString)
    }

    @Test
    fun doubleLinksTests() {
        val simple1 = SimpleJniMirror(0)
        val simple2 = SimpleJniMirror(0)

        assertNotEquals(simple1.objId(), simple2.objId())
    }

}
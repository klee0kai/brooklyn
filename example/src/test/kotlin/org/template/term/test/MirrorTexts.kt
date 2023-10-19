package org.template.term.test

import com.klee0kai.example.engine.SimpleJniEngine
import com.klee0kai.example.mirrors.SimpleJniMirror
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

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
    fun updateFromCppTest() {
        //given
        val simple = SimpleJniMirror(2)
        simple.someString = "some str"

        //when
        val result = simple.updateFromCpp(4, " update from cpp")

        //then
        assertEquals(1, result)
        assertEquals(6, simple.someInt)
        assertEquals("some str update from cpp", simple.someString)
    }

    @Test
    fun updateFromCppDirectlyTest() {
        //given
        val simple = SimpleJniMirror(2)
        simple.someString = "some str"

        //when
        val result = simple.updateFromCppDirectly(-4, " c++")

        //then
        assertEquals(2, result)
        assertEquals(-2, simple.someInt)
        assertEquals("some str c++", simple.someString)
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

    @Test
    fun createMirrorFromCpp1Tests() {
        val simpleMirror = SimpleJniEngine.createSimpleMirror1()
        assertEquals(41, simpleMirror.someInt)
    }

    @Test
    fun createMirrorFromCpp2Tests() {
        val simpleMirror = SimpleJniEngine.createSimpleMirror2()

        assertEquals("created from c++", simpleMirror.someString)
    }


    @Test
    fun gcCppMirror1Test() {
        val simpleMirror = WeakReference(SimpleJniEngine.createSimpleMirror1())
        System.gc()
        assertNull(simpleMirror.get())
    }

    @Test
    fun gcCppMirror2Test() {
        val simpleMirror = WeakReference(SimpleJniEngine.createSimpleMirror2())
        System.gc()
        assertNull(simpleMirror.get())
    }

    @Test
    fun holdSimpleMirrorTest() {
        val simple = WeakReference(SimpleJniMirror(0))
        SimpleJniEngine.holdSimpleMirror(simple.get()!!)

        System.gc()
        assertNotNull(simple.get())

        SimpleJniEngine.unHoldSimpleMirror()
        System.gc()
        assertNull(simple.get())
    }

}
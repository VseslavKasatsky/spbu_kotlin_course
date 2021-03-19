package action

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class CommandStorageTest {

    @Test
    fun serializeToFile() {
        val testStorage = CommandStorage()
        testStorage.serializeToFile("temp.json")
        assertEquals("[]", File("temp.json").readText())
        InsertAtStart(1).doAction(testStorage)
        InsertAtStart(2).doAction(testStorage)
        InsertAtStart(3).doAction(testStorage)
        InsertAtEnd(5).doAction(testStorage)
        testStorage.serializeToFile("temp.json")
        assertEquals(javaClass.getResource("ActionList.json").readText(), File("temp.json").readText())
        File("temp.json").delete()
    }

    @Test
    fun deserializeFromFile() {
        val testStorage = CommandStorage()
        testStorage.deserializeFromFile(javaClass.getResource("ActionList.json").path)
        assertEquals(listOf(3, 2, 1, 5), testStorage.numberList)
    }
}
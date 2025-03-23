package com.copetti.srcli.domain.usecase.file

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Files
import kotlin.io.path.deleteExisting

class FileSystemTest {

    private val fileSystem = FileSystem()

    @Test
    fun `should write and read files correctly`() {
        val file = Files.createTempFile("test-read-write", "")
        try {
            val content = "the-file-content"
            fileSystem.writeFile(file.toAbsolutePath(), content)
            val actual = fileSystem.readFile(file.toAbsolutePath())
            assertEquals(content, actual)
        } finally {
            file.deleteExisting()
        }
    }
}
package com.copetti

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.file.Files

class AppPropertiesTest {

    @Test
    fun `should load an empty file without any errors`() {
        val file = Files.createTempFile("", "")
        Files.writeString(file.toAbsolutePath(), "")

        val appProps = AppProperties.load(file.toAbsolutePath().toString())
        assertNull(appProps.getUsername())
        assertNull(appProps.getPassword())
    }

    @Test
    fun `should load username and password correctly`() {
        val content = """
            app.username=the-username
            app.password=the-password
        """.trimIndent()
        val file = Files.createTempFile("", "")
        Files.writeString(file.toAbsolutePath(), content)

        val appProps = AppProperties.load(file.toAbsolutePath().toString())
        assertEquals("the-username", appProps.getUsername())
        assertEquals("the-password", appProps.getPassword())
    }
}
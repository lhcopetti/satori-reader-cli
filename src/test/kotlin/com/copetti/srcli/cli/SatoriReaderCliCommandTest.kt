package com.copetti.srcli.cli

import com.github.ajalt.clikt.core.obj
import com.github.ajalt.clikt.testing.test
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SatoriReaderCliCommandTest {

    @InjectMockKs
    private lateinit var satoriReaderCliCommand: SatoriReaderCliCommand

    @Test
    fun `should set up context correctly`() {

        val result = satoriReaderCliCommand.test(
            "--username name --password pass"
        )
        assertEquals(0, result.statusCode)
        val context = satoriReaderCliCommand.currentContext.obj as SatoriReaderCliContext
        assertEquals("name", context.credentials.username)
        assertEquals("pass", context.credentials.password)
    }

    @Test
    fun `should prompt for password if not supplied`() {

        val result = satoriReaderCliCommand.test(
            argv = "--username name",
            stdin = "the-pass\n"
        )
        assertEquals(0, result.statusCode)
        val context = satoriReaderCliCommand.currentContext.obj as SatoriReaderCliContext
        assertEquals("name", context.credentials.username)
        assertEquals("the-pass", context.credentials.password)
    }
}
package com.copetti.srcli.cli

import com.copetti.srcli.domain.model.LoginApplicationCredentials
import com.copetti.srcli.domain.model.TokenApplicationCredentials
import com.github.ajalt.clikt.core.obj
import com.github.ajalt.clikt.testing.test
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertIs

@ExtendWith(MockKExtension::class)
class SatoriReaderCliCommandTest {

    @InjectMockKs
    private lateinit var satoriReaderCliCommand: SatoriReaderCliCommand

    @Test
    fun `should set up context correctly`() {
        val result = satoriReaderCliCommand.test(
            "--auth login --username name --password pass"
        )
        assertEquals(0, result.statusCode)
        val context = satoriReaderCliCommand.currentContext.obj as ApplicationCliContext
        assertIs<LoginApplicationCredentials>(context.credentials)
        assertEquals("name", context.credentials.username)
        assertEquals("pass", context.credentials.password)
    }

    @Test
    fun `should set up context correctly for token credentials`() {
        val result = satoriReaderCliCommand.test(
            "--auth token --token the-token"
        )
        assertEquals(0, result.statusCode)
        val context = satoriReaderCliCommand.currentContext.obj as ApplicationCliContext
        assertIs<TokenApplicationCredentials>(context.credentials)
        assertEquals("the-token", context.credentials.token)
    }

    @Test
    fun `should prompt for password if not supplied`() {
        val result = satoriReaderCliCommand.test(
            argv = "--auth login --username name",
            stdin = "the-pass\n"
        )
        assertEquals(0, result.statusCode)
        val context = satoriReaderCliCommand.currentContext.obj as ApplicationCliContext
        assertIs<LoginApplicationCredentials>(context.credentials)
        assertEquals("name", context.credentials.username)
        assertEquals("the-pass", context.credentials.password)
    }

    @Test
    fun `should prompt for token if not supplied`() {
        val result = satoriReaderCliCommand.test(
            argv = "--auth token",
            stdin = "the-token\n"
        )
        assertEquals(0, result.statusCode)
        val context = satoriReaderCliCommand.currentContext.obj as ApplicationCliContext
        assertIs<TokenApplicationCredentials>(context.credentials)
        assertEquals("the-token", context.credentials.token)
    }
}
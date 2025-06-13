package com.copetti.srcli.cli

import com.copetti.srcli.domain.model.LoginApplicationCredentials
import com.copetti.srcli.domain.usecase.cli.PrintAllEpisodeStatus
import com.copetti.srcli.domain.usecase.cli.PrintAllEpisodesRequest
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.obj
import com.github.ajalt.clikt.testing.test
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class PrintAllEpisodesCommandTest {

    @MockK
    private lateinit var printAllEpisodeStatus: PrintAllEpisodeStatus

    @InjectMockKs
    private lateinit var printAllEpisodesCommand: PrintAllEpisodesCommand

    @Test
    fun `should call the use case correctly`() {
        val credentials = LoginApplicationCredentials(username = "username", password = "password")

        every { printAllEpisodeStatus.print(any()) } returns "the-output"
        val result = printAllEpisodesCommand
            .context { obj = ApplicationCliContext(credentials = credentials) }
            .test()

        assertEquals("the-output\n", result.output)

        verify { printAllEpisodeStatus.print(PrintAllEpisodesRequest(credentials = credentials)) }
    }
}

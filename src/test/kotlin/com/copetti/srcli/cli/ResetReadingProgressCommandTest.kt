package com.copetti.srcli.cli

import com.copetti.srcli.domain.model.LoginApplicationCredentials
import com.copetti.srcli.domain.usecase.cli.ResetReadingProgress
import com.copetti.srcli.domain.usecase.cli.ResetReadingProgressRequest
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
class ResetReadingProgressCommandTest {

    @MockK
    private lateinit var resetReadingProgress: ResetReadingProgress

    @InjectMockKs
    private lateinit var resetReadingProgressCommand: ResetReadingProgressCommand

    @Test
    fun `should call the use case correctly`() {
        val credentials = LoginApplicationCredentials(username = "username", password = "password")

        every { resetReadingProgress.reset(any()) } returns Unit
        val result = resetReadingProgressCommand
            .context { obj = ApplicationCliContext(credentials = credentials) }
            .test()

        assertEquals("", result.output)

        verify { resetReadingProgress.reset(ResetReadingProgressRequest(credentials = credentials)) }
    }
}

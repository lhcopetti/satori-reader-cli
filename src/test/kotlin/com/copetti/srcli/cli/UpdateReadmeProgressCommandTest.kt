package com.copetti.srcli.cli

import com.copetti.srcli.domain.model.LoginApplicationCredentials
import com.copetti.srcli.domain.usecase.cli.UpdateReadmeProgress
import com.copetti.srcli.domain.usecase.cli.UpdateReadmeRequest
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
class UpdateReadmeProgressCommandTest {

    @MockK
    private lateinit var updateReadmeProgress: UpdateReadmeProgress

    @InjectMockKs
    private lateinit var updateReadingProgressCommand: UpdateReadmeProgressCommand

    @Test
    fun `should call the use case correctly`() {
        val credentials = LoginApplicationCredentials(username = "username", password = "password")

        every { updateReadmeProgress.update(any()) } returns Unit
        val result = updateReadingProgressCommand
            .context { obj = ApplicationCliContext(credentials = credentials) }
            .test()

        assertEquals("", result.output)

        verify { updateReadmeProgress.update(UpdateReadmeRequest(credentials = credentials)) }
    }
}

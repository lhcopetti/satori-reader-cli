package com.copetti.srcli.domain.usecase.progress

import com.copetti.srcli.domain.model.LoginApplicationCredentials
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class GenerateProgressDashboardTest {

    @MockK
    private lateinit var retrieveReadingProgress: RetrieveReadingProgress

    @MockK
    private lateinit var buildProgressDashboard: BuildProgressDashboard

    @InjectMockKs
    private lateinit var generateProgressDashboard: GenerateProgressDashboard

    @Test
    fun `should retrieve reading progress and then build the dashboard`() {
        val credentials = LoginApplicationCredentials(username = "the-username", password = "the-password")

        val progressionList = listOf(
            SeriesProgression(title = "the-title", link = "the-link", episodes = listOf())
        )
        every { retrieveReadingProgress.retrieve(any()) } returns progressionList
        every { buildProgressDashboard.build(any()) } returns "built-dashboard"

        val request = GenerateProgressDashboardRequest(credentials = credentials)
        val actual = generateProgressDashboard.generate(request)

        assertEquals("built-dashboard", actual)

        val retrieveRequest = RetrieveReadingProgressRequest(credentials)
        verify { retrieveReadingProgress.retrieve(retrieveRequest) }

        val buildRequest = BuildProgressDashboardRequest(progression = progressionList)
        verify { buildProgressDashboard.build(buildRequest) }
    }
}
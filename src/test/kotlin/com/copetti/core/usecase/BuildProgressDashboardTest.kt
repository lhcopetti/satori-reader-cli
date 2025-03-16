package com.copetti.core.usecase

import com.copetti.model.SatoriReaderStatus
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class BuildProgressDashboardTest {

    @MockK
    private lateinit var getProgressStatusMarker: GetProgressStatusMarker

    @InjectMockKs
    private lateinit var buildProgressDashboard: BuildProgressDashboard

    @Test
    fun `should build the progress dashboard correctly`() {

        every { getProgressStatusMarker.build(any()) } returns "X"

        val progress = createSeriesProgression(title = "the-title", completedCount = 2, unreadCount = 2)
        val request = BuildProgressDashboardRequest(listOf(progress))

        val expected = """

            ## Progression Dashboard

            X
            ### Series progression: (0/1) - 0.00%
            ### Episodes progression: (2/4) - 50.00%

        """.trimIndent()

        val actual = buildProgressDashboard.build(request)
        assertEquals(expected, actual)
    }

    @Test
    fun `should correctly generate a table for a multiple series`() {
        val progress = listOf(
            createSeriesProgression(title = "A", completedCount = 2, unreadCount = 5),
            createSeriesProgression(title = "B", completedCount = 3, unreadCount = 6),
            createSeriesProgression(title = "C", completedCount = 4, unreadCount = 7),
        )
        val request = BuildProgressDashboardRequest(progress)

        every { getProgressStatusMarker.build(any()) } returns "X"

        val expected = """

            ## Progression Dashboard

            X X X
            ### Series progression: (0/3) - 0.00%
            ### Episodes progression: (9/27) - 33.33%

        """.trimIndent()
        val actual = buildProgressDashboard.build(request)

        assertEquals(expected, actual)
    }

    private fun createSeriesProgression(title: String, completedCount: Int, unreadCount: Int): SeriesProgression {
        val completed = (1..completedCount).map { EpisodeProgression("the-title", SatoriReaderStatus.COMPLETED) }
        val unread = (1..unreadCount).map { EpisodeProgression("the-title", SatoriReaderStatus.UNREAD) }
        return SeriesProgression(title = title, episodes = completed + unread)
    }
}
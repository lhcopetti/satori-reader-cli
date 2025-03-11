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

    @BeforeEach
    fun setUp() {
        every { getProgressStatusMarker.get(SatoriReaderStatus.COMPLETED) } returns "O"
        every { getProgressStatusMarker.get(SatoriReaderStatus.STARTED) } returns "X"
        every { getProgressStatusMarker.get(SatoriReaderStatus.UNREAD) } returns "X"
    }

    @Test
    fun `should use the markers correctly`() {
        every { getProgressStatusMarker.get(SatoriReaderStatus.COMPLETED) } returns "COMP"
        every { getProgressStatusMarker.get(SatoriReaderStatus.STARTED) } returns "STAR"
        every { getProgressStatusMarker.get(SatoriReaderStatus.UNREAD) } returns "UNRE"

        val progress = createSeriesProgression(title = "the-title", completedCount = 2, unreadCount = 2)
        val request = BuildProgressDashboardRequest(listOf(progress))

        val expected = """
            ### Series progression: (0/1) - 0,00%
            ### Episodes progression: (2/4) - 50,00%

            ## Progression Dashboard

            COMPCOMPUNREUNRE

        """.trimIndent()



        val actual = buildProgressDashboard.build(request)

        for (byte in expected.toByteArray())
            print(String.format("%02x ", byte))
        println()
        for (byte in actual.toByteArray())
            print(String.format("%02x ", byte))
        println()

        assertEquals(expected, actual)
    }

    @Test
    fun `should correctly build a table for a single series`() {
        val progress = createSeriesProgression(title = "the-title", completedCount = 5, unreadCount = 3)
        val request = BuildProgressDashboardRequest(listOf(progress))

        val expected = """
            ### Series progression: (0/1) - 0,00%
            ### Episodes progression: (5/8) - 62,50%

            ## Progression Dashboard

            OOOOOXXX

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

        val expected = """
            ### Series progression: (0/3) - 0,00%
            ### Episodes progression: (9/27) - 33,33%

            ## Progression Dashboard

            OOXXXXX OOOXXXXXX OOOOXXXXXXX

        """.trimIndent()
        val actual = buildProgressDashboard.build(request)

        assertEquals(expected, actual)
    }

    @Test
    fun `should correctly generate a table for a multiple series in multiple tables`() {
        val progress = listOf(
            createSeriesProgression(title = "A", completedCount = 1, unreadCount = 0),
            createSeriesProgression(title = "B", completedCount = 2, unreadCount = 6),
            createSeriesProgression(title = "C", completedCount = 3, unreadCount = 5),
            createSeriesProgression(title = "D", completedCount = 4, unreadCount = 0),
            createSeriesProgression(title = "E", completedCount = 5, unreadCount = 3),
            createSeriesProgression(title = "F", completedCount = 6, unreadCount = 2),
            createSeriesProgression(title = "G", completedCount = 7, unreadCount = 1),
        )
        val request = BuildProgressDashboardRequest(progress)

        val expected = """
            ### Series progression: (2/7) - 28,57%
            ### Episodes progression: (28/45) - 62,22%

            ## Progression Dashboard

            O OOXXXXXX OOOXXXXX OOOO OOOOOXXX OOOOOOXX OOOOOOOX

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
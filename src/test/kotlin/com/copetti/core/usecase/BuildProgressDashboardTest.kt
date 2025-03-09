package com.copetti.core.usecase

import com.copetti.model.SatoriReaderStatus
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class BuildProgressDashboardTest {

    @InjectMockKs
    private lateinit var buildProgressDashboard: BuildProgressDashboard

    @Test
    fun `should correctly build a table for a single series`() {
        val progress = createSeriesProgression(title = "the-title", completedCount = 5, unreadCount = 3)
        val request = BuildProgressDashboardRequest(listOf(progress))

        val expected = """
            |the-title|
            |---|
            |OOOOOXXX|

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
            |A|B|C|
            |---|---|---|
            |OOXXXXX|OOOXXXXXX|OOOOXXXXXXX|

        """.trimIndent()
        val actual = buildProgressDashboard.build(request)

        assertEquals(expected, actual)
    }

    @Test
    fun `should correctly generate a table for a multiple series in multiple tables`() {
        val progress = listOf(
            createSeriesProgression(title = "A", completedCount = 1, unreadCount = 7),
            createSeriesProgression(title = "B", completedCount = 2, unreadCount = 6),
            createSeriesProgression(title = "C", completedCount = 3, unreadCount = 5),
            createSeriesProgression(title = "D", completedCount = 4, unreadCount = 4),
            createSeriesProgression(title = "E", completedCount = 5, unreadCount = 3),
            createSeriesProgression(title = "F", completedCount = 6, unreadCount = 2),
            createSeriesProgression(title = "G", completedCount = 7, unreadCount = 1),
        )
        val request = BuildProgressDashboardRequest(progress)

        val expected = """
            |A|B|C|D|E|
            |---|---|---|---|---|
            |OXXXXXXX|OOXXXXXX|OOOXXXXX|OOOOXXXX|OOOOOXXX|

            |F|G|
            |---|---|
            |OOOOOOXX|OOOOOOOX|

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
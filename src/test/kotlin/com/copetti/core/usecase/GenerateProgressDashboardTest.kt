package com.copetti.core.usecase

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GenerateProgressDashboardTest {

    private val generateProgressDashboard = GenerateProgressDashboard()

    @Test
    fun `should correctly generate a table for a single series`() {
        val progress = SeriesProgression(
            title = "the-title",
            completedCount = 5,
            unreadCount = 3
        )
        val request = GenerateProgressDashboardRequest(seriesProgression = listOf(progress))

        val expected = """
            |the-title|
            |---|
            |OOOOOXXX|

        """.trimIndent()
        val actual = generateProgressDashboard.generate(request)

        assertEquals(expected, actual)
    }

    @Test
    fun `should correctly generate a table for a multiple series`() {
        val progress = listOf(
            SeriesProgression(title = "A", completedCount = 2, unreadCount = 5),
            SeriesProgression(title = "B", completedCount = 3, unreadCount = 6),
            SeriesProgression(title = "C", completedCount = 4, unreadCount = 7)
        )
        val request = GenerateProgressDashboardRequest(seriesProgression = progress)

        val expected = """
            |A|B|C|
            |---|---|---|
            |OOXXXXX|OOOXXXXXX|OOOOXXXXXXX|

        """.trimIndent()
        val actual = generateProgressDashboard.generate(request)

        assertEquals(expected, actual)
    }

    @Test
    fun `should correctly generate a table for a multiple series in multiple tables`() {
        val progress = listOf(
            SeriesProgression(title = "A", completedCount = 1, unreadCount = 7),
            SeriesProgression(title = "B", completedCount = 2, unreadCount = 6),
            SeriesProgression(title = "C", completedCount = 3, unreadCount = 5),
            SeriesProgression(title = "D", completedCount = 4, unreadCount = 4),
            SeriesProgression(title = "E", completedCount = 5, unreadCount = 3),
            SeriesProgression(title = "F", completedCount = 6, unreadCount = 2),
            SeriesProgression(title = "G", completedCount = 7, unreadCount = 1),
        )
        val request = GenerateProgressDashboardRequest(seriesProgression = progress)

        val expected = """
            |A|B|C|D|E|
            |---|---|---|---|---|
            |OXXXXXXX|OOXXXXXX|OOOXXXXX|OOOOXXXX|OOOOOXXX|

            |F|G|
            |---|---|
            |OOOOOXX|OOOOOOOX|

        """.trimIndent()
        val actual = generateProgressDashboard.generate(request)

        assertEquals(expected, actual)
    }
}
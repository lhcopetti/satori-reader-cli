package com.copetti.srcli.domain.usecase.progress

import com.copetti.srcli.domain.model.SatoriReaderStatus
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
    private lateinit var seriesProgressionBuilder: SeriesProgressionBuilder
    @MockK
    private lateinit var progressionCellBuilder: ProgressionCellBuilder

    @InjectMockKs
    private lateinit var buildProgressDashboard: BuildProgressDashboard

    @BeforeEach
    fun setUp() {
        every { seriesProgressionBuilder.build(any()) } returns "X"
        every { progressionCellBuilder.buildSeriesCell(any(), any()) } returns "S"
        every { progressionCellBuilder.buildStatusCell(eq(SatoriReaderStatus.COMPLETED), any(), any()) } returns "Completed"
        every { progressionCellBuilder.buildStatusCell(eq(SatoriReaderStatus.STARTED), any(), any()) } returns "Started"
        every { progressionCellBuilder.buildStatusCell(eq(SatoriReaderStatus.UNREAD), any(), any()) } returns "Unread"
    }

    @Test
    fun `should build the progress dashboard correctly`() {

        val progress = createSeriesProgression(title = "the-title", completedCount = 2, unreadCount = 2)
        val request = BuildProgressDashboardRequest(listOf(progress))

        val expected = """

            ## Progression Dashboard

            X
            
            S - Series Title |
            Completed - Completed |
            Started - Started |
            Unread - Unread |
            *(hover over a cell for more information or click on it to go to the series/episode)*
            
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

        val expected = """

            ## Progression Dashboard

            X X X

            S - Series Title |
            Completed - Completed |
            Started - Started |
            Unread - Unread |
            *(hover over a cell for more information or click on it to go to the series/episode)*
            
            ### Series progression: (0/3) - 0.00%
            ### Episodes progression: (9/27) - 33.33%

        """.trimIndent()
        val actual = buildProgressDashboard.build(request)

        assertEquals(expected, actual)
    }

    private fun createSeriesProgression(title: String, completedCount: Int, unreadCount: Int): SeriesProgression {
        val completed = (1..completedCount).map {
            EpisodeProgression("the-title", link = "link", status = SatoriReaderStatus.COMPLETED)
        }
        val unread = (1..unreadCount).map {
            EpisodeProgression("the-title", link = "link", status = SatoriReaderStatus.UNREAD)
        }
        return SeriesProgression(title = title, link = "link", episodes = completed + unread)
    }
}
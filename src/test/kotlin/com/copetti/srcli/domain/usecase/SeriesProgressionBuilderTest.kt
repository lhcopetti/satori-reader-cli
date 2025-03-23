package com.copetti.srcli.domain.usecase

import com.copetti.srcli.domain.model.SatoriReaderStatus
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SeriesProgressionBuilderTest {
    @MockK
    private lateinit var progressionCellBuilder: ProgressionCellBuilder

    @InjectMockKs
    private lateinit var seriesProgressionBuilder: SeriesProgressionBuilder

    @BeforeEach
    fun setUp() {
        every { progressionCellBuilder.buildSeriesCell(any(), any()) } returns "[S]"
        every {
            progressionCellBuilder.buildStatusCell(
                eq(SatoriReaderStatus.COMPLETED),
                any(),
                any()
            )
        } returns "[Completed]"
        every {
            progressionCellBuilder.buildStatusCell(
                eq(SatoriReaderStatus.STARTED),
                any(),
                any()
            )
        } returns "[Started]"
        every { progressionCellBuilder.buildStatusCell(eq(SatoriReaderStatus.UNREAD), any(), any()) } returns "[Unread]"
    }

    @Test
    fun `should build the progress cells correctly`() {
        val series = SeriesProgression(
            title = "the-series-title",
            link = "series-link",
            episodes = listOf(
                EpisodeProgression(
                    title = "1-episode",
                    link = "link-episode1",
                    status = SatoriReaderStatus.COMPLETED
                ),
                EpisodeProgression(
                    title = "2-episode",
                    link = "link-episode2",
                    status = SatoriReaderStatus.STARTED
                ),
                EpisodeProgression(
                    title = "3-episode",
                    link = "link-episode3",
                    status = SatoriReaderStatus.UNREAD
                )
            )
        )

        val expected = """
        [S][Completed][Started][Unread]
        """.trimIndent()
        val actual = seriesProgressionBuilder.build(series)

        assertEquals(expected, actual)

        verify { progressionCellBuilder.buildSeriesCell(link = "series-link", tooltip = "Series | the-series-title") }
        verify {
            progressionCellBuilder.buildStatusCell(
                status = SatoriReaderStatus.COMPLETED,
                link = "link-episode1",
                tooltip = "the-series-title | 1-episode"
            )
        }
        verify {
            progressionCellBuilder.buildStatusCell(
                status = SatoriReaderStatus.STARTED,
                link = "link-episode2",
                tooltip = "the-series-title | 2-episode"
            )
        }
        verify {
            progressionCellBuilder.buildStatusCell(
                status = SatoriReaderStatus.UNREAD,
                link = "link-episode3",
                tooltip = "the-series-title | 3-episode"
            )
        }
    }

}
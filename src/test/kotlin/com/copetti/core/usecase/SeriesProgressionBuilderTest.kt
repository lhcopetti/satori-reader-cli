package com.copetti.core.usecase

import com.copetti.model.SatoriReaderStatus
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SeriesProgressionBuilderTest {

    @InjectMockKs
    private lateinit var seriesProgressionBuilder: SeriesProgressionBuilder


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
        [![#000000](https://placehold.co/15x15/000000/.png?text=. "Series | the-series-title")](series-link)
        [![#00ff00](https://placehold.co/15x15/00ff00/.png?text=. "the-series-title | 1-episode")](link-episode1)
        [![#ffff00](https://placehold.co/15x15/ffff00/.png?text=. "the-series-title | 2-episode")](link-episode2)
        [![#ff0000](https://placehold.co/15x15/ff0000/.png?text=. "the-series-title | 3-episode")](link-episode3)
        """.trimIndent().replace("\n", "")
        val actual = seriesProgressionBuilder.build(series)

        assertEquals(expected, actual)
    }

    @Test
    fun `should escape quotes and backlashes`() {
        val series = SeriesProgression(
            title = "series title with \"quotes\" and \\backlashes\\ ! Be careful",
            link = "series-link",
            episodes = listOf(
                EpisodeProgression(
                    title = "episode title with \"quotes\" and \\backlashes\\ ! Be careful",
                    link = "link-episode1",
                    status = SatoriReaderStatus.COMPLETED
                ),
                EpisodeProgression(
                    title = "2-episode",
                    link = "link-episode2",
                    status = SatoriReaderStatus.COMPLETED
                ),
            )
        )

        val expected = """
        [![#000000](https://placehold.co/15x15/000000/.png?text=. "Series | series title with \"quotes\" and \\backlashes\\ ! Be careful")](series-link)
        [![#00ff00](https://placehold.co/15x15/00ff00/.png?text=. "series title with \"quotes\" and \\backlashes\\ ! Be careful | episode title with \"quotes\" and \\backlashes\\ ! Be careful")](link-episode1)
        [![#00ff00](https://placehold.co/15x15/00ff00/.png?text=. "series title with \"quotes\" and \\backlashes\\ ! Be careful | 2-episode")](link-episode2)
        """.trimIndent().replace("\n", "")
        val actual = seriesProgressionBuilder.build(series)

        assertEquals(expected, actual)
    }
}
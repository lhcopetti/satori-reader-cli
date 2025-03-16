package com.copetti.core.usecase

import com.copetti.model.SatoriReaderStatus
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class GetProgressStatusMarkerTest {

    @InjectMockKs
    private lateinit var getProgressStatusMarker: GetProgressStatusMarker


    @Test
    fun `should build the progress cells correctly`() {
        val series = SeriesProgression(
            title = "the-series-title",
            episodes = listOf(
                EpisodeProgression(
                    title = "1-episode",
                    status = SatoriReaderStatus.COMPLETED
                ),
                EpisodeProgression(
                    title = "2-episode",
                    status = SatoriReaderStatus.STARTED
                ),
                EpisodeProgression(
                    title = "3-episode",
                    status = SatoriReaderStatus.UNREAD
                )
            )
        )

        val expected = """
        [![#000000](https://placehold.co/15x15/000000/.png?text=. "the-series-title")](https://youtube.com)
        [![#00ff00](https://placehold.co/15x15/00ff00/.png?text=. "1-episode")](https://google.com)
        [![#ffff00](https://placehold.co/15x15/ffff00/.png?text=. "2-episode")](https://google.com)
        [![#ff0000](https://placehold.co/15x15/ff0000/.png?text=. "3-episode")](https://google.com)
        """.trimIndent().replace("\n", "")
        val actual = getProgressStatusMarker.build(series)

        assertEquals(expected, actual)
    }
}
package com.copetti.core.usecase

import com.copetti.model.*
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class RetrieveReadingProgressTest {

    @MockK
    private lateinit var fetchAllContent: FetchAllContent

    @MockK
    private lateinit var selectPrimaryEdition: SelectPrimaryEdition

    @InjectMockKs
    private lateinit var retrieveReadingProgress: RetrieveReadingProgress

    @Test
    fun `should retrieve all series and select the primary edition for each episode`() {
        val credentials = SatoriReaderCredentials(username = "the-username", password = "the-password")

        val firstEpisodeA = SatoriReaderEpisode(title = "A:episode 1", editions = listOf())
        val firstEpisodeAEdition =
            SatoriReaderEdition(name = "A:episode1", url = "url", status = SatoriReaderStatus.COMPLETED)
        every { selectPrimaryEdition.select(firstEpisodeA) } returns firstEpisodeAEdition

        val secondEpisodeA = SatoriReaderEpisode(title = "A:episode 2", editions = listOf())
        val secondEpisodeAEdition =
            SatoriReaderEdition(name = "A:episode2", url = "url", status = SatoriReaderStatus.UNREAD)
        every { selectPrimaryEdition.select(secondEpisodeA) } returns secondEpisodeAEdition

        val seriesA = SatoriReaderSeriesContent(title = "A", episodes = listOf(firstEpisodeA, secondEpisodeA))

        val firstEpisodeB = SatoriReaderEpisode(title = "B:episode 1", editions = listOf())
        val firstEpisodeBEdition =
            SatoriReaderEdition(name = "B:episode1", url = "url", status = SatoriReaderStatus.STARTED)
        every { selectPrimaryEdition.select(firstEpisodeB) } returns firstEpisodeBEdition

        val seriesB = SatoriReaderSeriesContent(title = "B", episodes = listOf(firstEpisodeB))

        every { fetchAllContent.fetchAllContent(any()) } returns listOf(seriesA, seriesB)

        val request = RetrieveReadingProgressRequest(credentials = credentials)
        val actual = retrieveReadingProgress.retrieve(request)

        val expected = listOf(
            SeriesProgression(
                title = "A", episodes = listOf(
                    EpisodeProgression(title = "A:episode 1", status = SatoriReaderStatus.COMPLETED),
                    EpisodeProgression(title = "A:episode 2", status = SatoriReaderStatus.UNREAD),
                )
            ),
            SeriesProgression(
                title = "B", episodes = listOf(
                    EpisodeProgression(title = "B:episode 1", status = SatoriReaderStatus.STARTED),
                )
            )
        )

        assertEquals(expected, actual)

        verify { fetchAllContent.fetchAllContent(FetchAllContentRequest(credentials)) }
        verify { selectPrimaryEdition.select(firstEpisodeA) }
        verify { selectPrimaryEdition.select(secondEpisodeA) }
        verify { selectPrimaryEdition.select(firstEpisodeB) }
    }
}
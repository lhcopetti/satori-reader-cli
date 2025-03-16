package com.copetti.core.usecase

import com.copetti.core.gateway.SatoriReaderProvider
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
    private lateinit var satoriReaderProvider: SatoriReaderProvider
    @MockK
    private lateinit var retrieveAllSatoriReaderSeries: RetrieveAllSatoriReaderSeries

    @InjectMockKs
    private lateinit var retrieveReadingProgress: RetrieveReadingProgress

    @Test
    fun `should retrieve all series and select the primary edition for each episode`() {
        val token = SatoriReaderLoginToken(sessionToken = "token")
        val credentials = SatoriReaderCredentials(username = "the-username", password = "the-password")

        val firstEpisodeAEdition =
            SatoriReaderEdition(name = "A:episode1", urlPath = "url", link= "link", status = SatoriReaderStatus.COMPLETED)
        val firstEpisodeA = SatoriReaderPrimaryEditionEpisode(title = "A:episode 1", edition = firstEpisodeAEdition)

        val secondEpisodeAEdition =
            SatoriReaderEdition(name = "A:episode2", urlPath = "url", link="link", status = SatoriReaderStatus.UNREAD)
        val secondEpisodeA = SatoriReaderPrimaryEditionEpisode(title = "A:episode 2", edition = secondEpisodeAEdition)

        val seriesA = SatoriReaderSeries(title = "A", episodes = listOf(firstEpisodeA, secondEpisodeA))

        val firstEpisodeBEdition =
            SatoriReaderEdition(name = "B:episode1", urlPath = "url", link = "link", status = SatoriReaderStatus.STARTED)
        val firstEpisodeB = SatoriReaderPrimaryEditionEpisode(title = "B:episode 1", edition = firstEpisodeBEdition)

        val seriesB = SatoriReaderSeries(title = "B", episodes = listOf(firstEpisodeB))

        every { retrieveAllSatoriReaderSeries.retrieve(any()) } returns listOf(seriesA, seriesB)
        every { satoriReaderProvider.login(credentials) } returns token

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

        verify { satoriReaderProvider.login(request.credentials) }
        verify { retrieveAllSatoriReaderSeries.retrieve(RetrieveAllSatoriReaderSeriesRequest(token = token)) }
    }
}
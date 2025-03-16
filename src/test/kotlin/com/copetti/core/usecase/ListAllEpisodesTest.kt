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
class ListAllEpisodesTest {
    @MockK
    private lateinit var satoriReaderProvider: SatoriReaderProvider

    @MockK
    private lateinit var retrieveAllSatoriReaderSeries: RetrieveAllSatoriReaderSeries

    @InjectMockKs
    private lateinit var listAllEpisodes: ListAllEpisodes


    @Test
    fun `should retrieve all series and list all the episodes`() {
        val credentials = SatoriReaderCredentials(
            username = "username",
            password = "password"
        )
        val token = SatoriReaderLoginToken(sessionToken = "token")

        val firstEpisodeAEdition =
            SatoriReaderEdition(
                name = "A:episode1",
                urlPath = "urlA1",
                link = "",
                status = SatoriReaderStatus.COMPLETED
            )
        val firstEpisodeA = SatoriReaderPrimaryEditionEpisode(title = "A:episode 1", edition = firstEpisodeAEdition)

        val secondEpisodeAEdition =
            SatoriReaderEdition(name = "A:episode2", urlPath = "urlA2", link = "", status = SatoriReaderStatus.UNREAD)
        val secondEpisodeA = SatoriReaderPrimaryEditionEpisode(title = "A:episode 2", edition = secondEpisodeAEdition)

        val seriesA = SatoriReaderSeries(title = "A", link = "linkA", episodes = listOf(firstEpisodeA, secondEpisodeA))

        val firstEpisodeBEdition =
            SatoriReaderEdition(name = "B:episode1", urlPath = "urlB1", link = "", status = SatoriReaderStatus.STARTED)
        val firstEpisodeB = SatoriReaderPrimaryEditionEpisode(title = "B:episode 1", edition = firstEpisodeBEdition)

        val seriesB = SatoriReaderSeries(title = "B", link= "linkB", episodes = listOf(firstEpisodeB))

        every { satoriReaderProvider.login(any()) } returns token
        every { retrieveAllSatoriReaderSeries.retrieve(any()) } returns listOf(seriesA, seriesB)

        val request = ListAllEpisodesRequest(credentials)
        val actual = listAllEpisodes.list(request)

        val expected = listOf(
            EpisodeStatus(title = "A", edition = "A:episode1", status = SatoriReaderStatus.COMPLETED, link = "urlA1"),
            EpisodeStatus(title = "A", edition = "A:episode2", status = SatoriReaderStatus.UNREAD, link = "urlA2"),
            EpisodeStatus(title = "B", edition = "B:episode1", status = SatoriReaderStatus.STARTED, link = "urlB1")
        )

        assertEquals(expected, actual)

        verify { satoriReaderProvider.login(request.credentials) }
        verify { retrieveAllSatoriReaderSeries.retrieve(RetrieveAllSatoriReaderSeriesRequest(token)) }

    }
}

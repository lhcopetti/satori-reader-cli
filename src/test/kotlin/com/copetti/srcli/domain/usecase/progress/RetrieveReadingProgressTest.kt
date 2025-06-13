package com.copetti.srcli.domain.usecase.progress

import com.copetti.srcli.domain.model.*
import com.copetti.srcli.domain.usecase.RetrieveAllSatoriReaderSeries
import com.copetti.srcli.domain.usecase.RetrieveAllSatoriReaderSeriesRequest
import com.copetti.srcli.domain.usecase.cli.AuthenticateUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class RetrieveReadingProgressTest {

    @MockK
    private lateinit var authenticateUser: AuthenticateUser

    @MockK
    private lateinit var retrieveAllSatoriReaderSeries: RetrieveAllSatoriReaderSeries

    @InjectMockKs
    private lateinit var retrieveReadingProgress: RetrieveReadingProgress

    @Test
    fun `should retrieve all series and select the primary edition for each episode`() = runTest {
        val token = SatoriReaderLoginToken(sessionToken = "token")
        val credentials = LoginApplicationCredentials(username = "the-username", password = "the-password")

        val firstEpisodeAEdition =
            SatoriReaderEdition(
                name = "A:episode1",
                urlPath = "url",
                link = "link-edition1A",
                status = SatoriReaderStatus.COMPLETED
            )
        val firstEpisodeA = SatoriReaderPrimaryEditionEpisode(title = "A:episode 1", edition = firstEpisodeAEdition)

        val secondEpisodeAEdition =
            SatoriReaderEdition(
                name = "A:episode2",
                urlPath = "url",
                link = "link-edition2A",
                status = SatoriReaderStatus.UNREAD
            )
        val secondEpisodeA = SatoriReaderPrimaryEditionEpisode(title = "A:episode 2", edition = secondEpisodeAEdition)

        val seriesA = SatoriReaderSeries(title = "A", link = "linkA", episodes = listOf(firstEpisodeA, secondEpisodeA))

        val firstEpisodeBEdition =
            SatoriReaderEdition(
                name = "B:episode1",
                urlPath = "url",
                link = "link-edition1B",
                status = SatoriReaderStatus.STARTED
            )
        val firstEpisodeB = SatoriReaderPrimaryEditionEpisode(title = "B:episode 1", edition = firstEpisodeBEdition)

        val seriesB = SatoriReaderSeries(title = "B", link = "linkB", episodes = listOf(firstEpisodeB))

        every { retrieveAllSatoriReaderSeries.retrieve(any()) } returns listOf(seriesA, seriesB)
        coEvery { authenticateUser.authenticate(credentials) } returns token

        val request = RetrieveReadingProgressRequest(credentials = credentials)
        val actual = retrieveReadingProgress.retrieve(request)

        val expected = listOf(
            SeriesProgression(
                title = "A", link = "linkA", episodes = listOf(
                    EpisodeProgression(
                        title = "A:episode 1",
                        link = "link-edition1A",
                        status = SatoriReaderStatus.COMPLETED
                    ),
                    EpisodeProgression(
                        title = "A:episode 2",
                        link = "link-edition2A",
                        status = SatoriReaderStatus.UNREAD
                    ),
                )
            ),
            SeriesProgression(
                title = "B", link = "linkB", episodes = listOf(
                    EpisodeProgression(
                        title = "B:episode 1",
                        link = "link-edition1B",
                        status = SatoriReaderStatus.STARTED
                    ),
                )
            )
        )

        assertEquals(expected, actual)

        coVerify { authenticateUser.authenticate(request.credentials) }
        verify { retrieveAllSatoriReaderSeries.retrieve(RetrieveAllSatoriReaderSeriesRequest(token = token)) }
    }
}
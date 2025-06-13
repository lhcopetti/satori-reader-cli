package com.copetti.srcli.domain.usecase.cli

import com.copetti.srcli.domain.model.*
import com.copetti.srcli.domain.usecase.RetrieveAllSatoriReaderSeries
import com.copetti.srcli.domain.usecase.RetrieveAllSatoriReaderSeriesRequest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class PrintAllEpisodeStatusTest {
    @MockK
    private lateinit var authenticateUser: AuthenticateUser

    @MockK
    private lateinit var retrieveAllSatoriReaderSeries: RetrieveAllSatoriReaderSeries

    @InjectMockKs
    private lateinit var printAllEpisodeStatus: PrintAllEpisodeStatus

    @Test
    fun `should retrieve all series and list all the episodes`() {
        val credentials = LoginApplicationCredentials(
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

        val seriesB = SatoriReaderSeries(title = "B", link = "linkB", episodes = listOf(firstEpisodeB))

        coEvery { authenticateUser.authenticate(any()) } returns token
        every { retrieveAllSatoriReaderSeries.retrieve(any()) } returns listOf(seriesA, seriesB)

        val request = PrintAllEpisodesRequest(credentials)
        val actual = printAllEpisodeStatus.print(request)

        val expected = """
            A,A:episode1,COMPLETED,urlA1
            A,A:episode2,UNREAD,urlA2
            B,B:episode1,STARTED,urlB1
        """.trimIndent()

        assertEquals(expected, actual)

        coVerify { authenticateUser.authenticate(request.credentials) }
        verify { retrieveAllSatoriReaderSeries.retrieve(RetrieveAllSatoriReaderSeriesRequest(token)) }
    }
}

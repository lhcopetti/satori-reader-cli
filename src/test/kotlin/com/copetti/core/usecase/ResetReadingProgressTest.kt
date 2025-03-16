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

@ExtendWith(MockKExtension::class)
class ResetReadingProgressTest {

    @MockK
    private lateinit var satoriReaderProvider: SatoriReaderProvider

    @MockK
    private lateinit var fetchAllSatoriReaderContent: FetchAllSatoriReaderContent

    @InjectMockKs
    private lateinit var resetReadingProgress: ResetReadingProgress

    @Test
    fun `should reset the progress for all episodes, series and editions`() {
        val credentials = SatoriReaderCredentials(username = "the-username", password = "the-password")
        val token = SatoriReaderLoginToken(sessionToken = "token")

        val firstEpisodeAEdition =
            SatoriReaderEdition(
                name = "A:episode1",
                urlPath = "urlA1",
                link = "link",
                status = SatoriReaderStatus.COMPLETED
            )
        val firstEpisodeA = SatoriReaderEpisode(title = "A:episode 1", editions = listOf(firstEpisodeAEdition))
        val seriesA = SatoriReaderSeriesContent(
            title = "A",
            link = "link",
            episodes = listOf(firstEpisodeA)
        )

        val episodeBFirstEdition =
            SatoriReaderEdition(name = "B:episode1", urlPath = "urlB1", link = "", status = SatoriReaderStatus.STARTED)
        val episodeBSecondEdition =
            SatoriReaderEdition(name = "B:episode1", urlPath = "urlB1", link = "", status = SatoriReaderStatus.STARTED)
        val episodeBThirdEdition =
            SatoriReaderEdition(name = "B:episode1", urlPath = "urlB1", link = "", status = SatoriReaderStatus.STARTED)
        val firstEpisodeB = SatoriReaderEpisode(
            title = "B:episode 1",
            editions = listOf(episodeBFirstEdition, episodeBSecondEdition, episodeBThirdEdition)
        )

        val seriesB = SatoriReaderSeriesContent(
            title = "B",
            link = "link",
            episodes = listOf(firstEpisodeB)
        )

        every { satoriReaderProvider.login(credentials) } returns token
        every { satoriReaderProvider.resetReadingProgress(any()) } returns Unit
        every { fetchAllSatoriReaderContent.fetch(any()) } returns listOf(seriesA, seriesB)

        resetReadingProgress.reset(ResetReadingProgressRequest(credentials = credentials))

        verify {
            satoriReaderProvider.resetReadingProgress(ResetEditionReadingProgressRequest(token, firstEpisodeAEdition))
        }
        verify {
            satoriReaderProvider.resetReadingProgress(ResetEditionReadingProgressRequest(token, episodeBFirstEdition))
        }
        verify {
            satoriReaderProvider.resetReadingProgress(ResetEditionReadingProgressRequest(token, episodeBSecondEdition))
        }
        verify {
            satoriReaderProvider.resetReadingProgress(ResetEditionReadingProgressRequest(token, episodeBThirdEdition))
        }
    }

}
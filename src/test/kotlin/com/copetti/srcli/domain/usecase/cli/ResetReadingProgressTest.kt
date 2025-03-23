package com.copetti.srcli.domain.usecase.cli

import com.copetti.srcli.domain.gateway.SatoriReaderProvider
import com.copetti.srcli.domain.model.*
import com.copetti.srcli.provider.DelayedSatoriReaderProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.time.Duration

@ExtendWith(MockKExtension::class)
class ResetReadingProgressTest {

    @MockK
    private lateinit var satoriReaderProvider: SatoriReaderProvider

    @InjectMockKs
    private lateinit var resetReadingProgress: ResetReadingProgress

    @Test
    fun `should reset the progress for all episodes, series and editions`() = runTest {
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

        val referenceSeriesA = SatoriReaderSeriesReference(link = "linkToA")
        val referenceSeriesB = SatoriReaderSeriesReference(link = "linkToB")

        coEvery { satoriReaderProvider.fetchSeries() } returns listOf(referenceSeriesA, referenceSeriesB)
        coEvery {
            satoriReaderProvider.fetchSeriesContent(
                FetchSeriesContentRequest(
                    token,
                    referenceSeriesA
                )
            )
        } returns seriesA
        coEvery {
            satoriReaderProvider.fetchSeriesContent(
                FetchSeriesContentRequest(
                    token,
                    referenceSeriesB
                )
            )
        } returns seriesB

        coEvery { satoriReaderProvider.login(credentials) } returns token
        coEvery { satoriReaderProvider.resetReadingProgress(any()) } returns Unit

        resetReadingProgress.reset(ResetReadingProgressRequest(credentials = credentials))

        coVerify {
            satoriReaderProvider.resetReadingProgress(ResetEditionReadingProgressRequest(token, firstEpisodeAEdition))
        }
        coVerify {
            satoriReaderProvider.resetReadingProgress(ResetEditionReadingProgressRequest(token, episodeBFirstEdition))
        }
        coVerify {
            satoriReaderProvider.resetReadingProgress(ResetEditionReadingProgressRequest(token, episodeBSecondEdition))
        }
        coVerify {
            satoriReaderProvider.resetReadingProgress(ResetEditionReadingProgressRequest(token, episodeBThirdEdition))
        }
    }

    @Test
    fun `should parallelize execution for resetting progress`() = runTest(timeout = Duration.parse("1s")) {
        val delayedSatoriReaderProvider = DelayedSatoriReaderProvider(
            numberOfEpisodes = 10,
            numberOfEditionsPerEpisode = 30,
            delayMs = 1
        )
        val resetReadingProgress = ResetReadingProgress(delayedSatoriReaderProvider)

        val credentials = SatoriReaderCredentials(username = "the-username", password = "the-password")

        resetReadingProgress.reset(ResetReadingProgressRequest(credentials = credentials))
    }

}
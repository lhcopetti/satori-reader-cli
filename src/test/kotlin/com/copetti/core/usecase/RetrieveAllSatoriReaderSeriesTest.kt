package com.copetti.core.usecase

import com.copetti.model.*
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class RetrieveAllSatoriReaderSeriesTest {

    @MockK
    private lateinit var fetchAllSatoriReaderContent: FetchAllSatoriReaderContent

    @MockK
    private lateinit var selectPrimaryEdition: SelectPrimaryEdition

    @InjectMockKs
    private lateinit var retrieveAllSatoriReaderSeries: RetrieveAllSatoriReaderSeries

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

        every { fetchAllSatoriReaderContent.fetchAllContent(any()) } returns listOf(seriesA, seriesB)

        val request = RetrieveAllSatoriReaderSeriesRequest(credentials = credentials)
        val actual = retrieveAllSatoriReaderSeries.retrieve(request)

        val expected = listOf(
            SatoriReaderSeries(
                title = "A", episodes = listOf(
                    SatoriReaderPrimaryEditionEpisode(title = "A:episode 1", edition = firstEpisodeAEdition),
                    SatoriReaderPrimaryEditionEpisode(title = "A:episode 2", edition = secondEpisodeAEdition),
                )
            ),
            SatoriReaderSeries(
                title = "B", episodes = listOf(
                    SatoriReaderPrimaryEditionEpisode(title = "B:episode 1", edition = firstEpisodeBEdition),
                )
            )
        )

        assertEquals(expected, actual)

        verify { fetchAllSatoriReaderContent.fetchAllContent(FetchAllSatoriReaderContentRequest(credentials)) }
        verify { selectPrimaryEdition.select(firstEpisodeA) }
        verify { selectPrimaryEdition.select(secondEpisodeA) }
        verify { selectPrimaryEdition.select(firstEpisodeB) }
    }


}
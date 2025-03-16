package com.copetti.srcli.domain.usecase

import com.copetti.srcli.domain.model.*
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
        val token = SatoriReaderLoginToken(sessionToken = "token")

        val firstEpisodeA = SatoriReaderEpisode(title = "A:episode 1", editions = listOf())
        val firstEpisodeAEdition =
            SatoriReaderEdition(
                name = "A:episode1",
                urlPath = "url-edition1A",
                link = "link-edition1A",
                status = SatoriReaderStatus.COMPLETED
            )
        every { selectPrimaryEdition.select(firstEpisodeA) } returns firstEpisodeAEdition

        val secondEpisodeA = SatoriReaderEpisode(title = "A:episode 2", editions = listOf())
        val secondEpisodeAEdition =
            SatoriReaderEdition(
                name = "A:episode2",
                urlPath = "url-edition2A",
                link = "link-edition2A",
                status = SatoriReaderStatus.UNREAD
            )
        every { selectPrimaryEdition.select(secondEpisodeA) } returns secondEpisodeAEdition

        val seriesA = SatoriReaderSeriesContent(
            title = "A",
            link = "link-seriesA",
            episodes = listOf(firstEpisodeA, secondEpisodeA)
        )

        val firstEpisodeB = SatoriReaderEpisode(title = "B:episode 1", editions = listOf())
        val firstEpisodeBEdition =
            SatoriReaderEdition(
                name = "B:episode1",
                urlPath = "url",
                link = "link-edition1B",
                status = SatoriReaderStatus.STARTED
            )
        every { selectPrimaryEdition.select(firstEpisodeB) } returns firstEpisodeBEdition

        val seriesB = SatoriReaderSeriesContent(
            title = "B",
            link = "link-seriesB",
            episodes = listOf(firstEpisodeB)
        )

        every { fetchAllSatoriReaderContent.fetch(any()) } returns listOf(seriesA, seriesB)

        val request = RetrieveAllSatoriReaderSeriesRequest(token = token)
        val actual = retrieveAllSatoriReaderSeries.retrieve(request)

        val expected = listOf(
            SatoriReaderSeries(
                title = "A", link = "link-seriesA", episodes = listOf(
                    SatoriReaderPrimaryEditionEpisode(title = "A:episode 1", edition = firstEpisodeAEdition),
                    SatoriReaderPrimaryEditionEpisode(title = "A:episode 2", edition = secondEpisodeAEdition),
                )
            ),
            SatoriReaderSeries(
                title = "B", link = "link-seriesB", episodes = listOf(
                    SatoriReaderPrimaryEditionEpisode(title = "B:episode 1", edition = firstEpisodeBEdition),
                )
            )
        )

        assertEquals(expected, actual)

        verify { fetchAllSatoriReaderContent.fetch(FetchAllSatoriReaderContentRequest(token = token)) }
        verify { selectPrimaryEdition.select(firstEpisodeA) }
        verify { selectPrimaryEdition.select(secondEpisodeA) }
        verify { selectPrimaryEdition.select(firstEpisodeB) }
    }


}
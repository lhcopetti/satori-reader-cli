package com.copetti.srcli.domain.usecase

import com.copetti.srcli.domain.gateway.SatoriReaderProvider
import com.copetti.srcli.domain.model.FetchSeriesContentRequest
import com.copetti.srcli.domain.model.SatoriReaderLoginToken
import com.copetti.srcli.domain.model.SatoriReaderSeriesContent
import com.copetti.srcli.domain.model.SatoriReaderSeriesReference
import com.copetti.srcli.provider.DelayedSatoriReaderProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.time.Duration

@ExtendWith(MockKExtension::class)
class FetchAllSatoriReaderContentTest {

    @MockK
    private lateinit var provider: SatoriReaderProvider

    @InjectMockKs
    private lateinit var fetchAllSatoriReaderContent: FetchAllSatoriReaderContent

    @Test
    fun `should fetch all series content`() = runTest {
        val token = SatoriReaderLoginToken(sessionToken = "the-token")

        val seriesA = SatoriReaderSeriesReference(link = "A")
        val seriesB = SatoriReaderSeriesReference(link = "B")

        coEvery { provider.fetchSeries() } returns listOf(seriesA, seriesB)

        val contentA = SatoriReaderSeriesContent(title = "title A", link = "link", episodes = listOf())
        val contentB = SatoriReaderSeriesContent(title = "title B", link = "link", episodes = listOf())

        coEvery { provider.fetchSeriesContent(any()) } returns contentA andThen contentB

        val request = FetchAllSatoriReaderContentRequest(token = token)
        val actual = fetchAllSatoriReaderContent.fetch(request)

        val expected = listOf(contentA, contentB)
        assertEquals(expected, actual)

        coVerify { provider.fetchSeries() }
        coVerify { provider.fetchSeriesContent(FetchSeriesContentRequest(token = token, series = seriesA)) }
        coVerify { provider.fetchSeriesContent(FetchSeriesContentRequest(token = token, series = seriesB)) }
    }


    @Test
    fun `should fetch each series content asynchronously`() = runTest(timeout = Duration.parse("250ms")) {
        val delayedSatoriReaderProvider = DelayedSatoriReaderProvider(
            numberOfEpisodes = 20,
            numberOfEditionsPerEpisode = 10,
            delayMs = 100
        )
        val fetchAllSatoriReaderContent = FetchAllSatoriReaderContent(delayedSatoriReaderProvider)

        val request = FetchAllSatoriReaderContentRequest(token = SatoriReaderLoginToken(sessionToken = ""))
        fetchAllSatoriReaderContent.fetch(request)

    }
}
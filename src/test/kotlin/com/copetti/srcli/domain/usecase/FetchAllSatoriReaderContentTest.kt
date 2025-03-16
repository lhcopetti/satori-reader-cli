package com.copetti.srcli.domain.usecase

import com.copetti.srcli.domain.gateway.SatoriReaderProvider
import com.copetti.srcli.domain.model.*
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class FetchAllSatoriReaderContentTest {

    @MockK
    private lateinit var provider: SatoriReaderProvider

    @InjectMockKs
    private lateinit var fetchAllSatoriReaderContent: FetchAllSatoriReaderContent

    @Test
    fun `should fetch all series content`() {
        val credentials = SatoriReaderCredentials(username = "the-username", password = "the-password")
        val token = SatoriReaderLoginToken(sessionToken = "the-token")

        val seriesA = SatoriReaderSeriesReference(link = "A")
        val seriesB = SatoriReaderSeriesReference(link = "B")

        every { provider.fetchSeries() } returns listOf(seriesA, seriesB)

        val contentA = SatoriReaderSeriesContent(title = "title A", link = "link", episodes = listOf())
        val contentB = SatoriReaderSeriesContent(title = "title B", link = "link", episodes = listOf())

        every { provider.fetchSeriesContent(any()) } returns contentA andThen contentB

        val request = FetchAllSatoriReaderContentRequest(token = token)
        val actual = fetchAllSatoriReaderContent.fetch(request)

        val expected = listOf(contentA, contentB)
        assertEquals(expected, actual)

        verify { provider.fetchSeries() }
        verify { provider.fetchSeriesContent(FetchSeriesContentRequest(token = token, series = seriesA)) }
        verify { provider.fetchSeriesContent(FetchSeriesContentRequest(token = token, series = seriesB)) }
    }
}
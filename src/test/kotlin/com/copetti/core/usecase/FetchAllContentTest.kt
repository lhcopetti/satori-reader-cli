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
class FetchAllContentTest {

    @MockK
    private lateinit var provider: SatoriReaderProvider

    @InjectMockKs
    private lateinit var fetchAllContent: FetchAllContent

    @Test
    fun `should fetch all series content`() {
        val credentials = SatoriReaderCredentials(username = "the-username", password = "the-password")
        val token = SatoriReaderLoginToken(sessionToken = "the-token")

        every { provider.login(credentials) } returns token

        val seriesA = SatoriReaderSeries(link = "A")
        val seriesB = SatoriReaderSeries(link = "B")

        every { provider.fetchSeries() } returns listOf(seriesA, seriesB)

        val contentA = SatoriReaderSeriesContent(title = "title A", episodes = listOf())
        val contentB = SatoriReaderSeriesContent(title = "title B", episodes = listOf())

        every { provider.fetchSeriesContent(any()) } returns contentA andThen contentB

        val request = FetchAllContentRequest(credentials = credentials)
        val actual = fetchAllContent.fetchAllContent(request)

        val expected = listOf(contentA, contentB)
        assertEquals(expected, actual)

        verify { provider.login(credentials) }
        verify { provider.fetchSeries() }
        verify { provider.fetchSeriesContent(FetchSeriesContentRequest(token = token, series = seriesA)) }
        verify { provider.fetchSeriesContent(FetchSeriesContentRequest(token = token, series = seriesB)) }
    }
}
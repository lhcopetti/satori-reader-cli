package com.copetti.provider.satori

import com.copetti.provider.satori.selenium.SeleniumSatoriReaderProvider
import com.copetti.provider.satori.webcrawler.WebCrawlerSatoriReaderProvider
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SatoriReaderProviderLocatorTest {

    @MockK
    private lateinit var webCrawlerSatoriReaderProvider: WebCrawlerSatoriReaderProvider
    @MockK
    private lateinit var seleniumSatoriReaderProvider: SeleniumSatoriReaderProvider

    @InjectMockKs
    private lateinit var satoriReaderProviderLocator: SatoriReaderProviderLocator

    @Test
    fun `return webcrawler when quiet flag is used`() {

        val request = SatoriReaderProviderLocatorRequest(quiet = true)
        val result = satoriReaderProviderLocator.locate(request)

        assertEquals(webCrawlerSatoriReaderProvider, result)
    }

    @Test
    fun `return selenium when quiet flag is false`() {

        val request = SatoriReaderProviderLocatorRequest(quiet = false)
        val result = satoriReaderProviderLocator.locate(request)

        assertEquals(seleniumSatoriReaderProvider, result)
    }
}
package com.copetti.provider.satori

import com.copetti.core.gateway.SatoriReaderProvider
import com.copetti.provider.satori.selenium.SeleniumSatoriReaderProvider
import com.copetti.provider.satori.webcrawler.WebCrawlerSatoriReaderProvider

data class SatoriReaderProviderLocatorRequest(
    val quiet: Boolean
)

class SatoriReaderProviderLocator(
    private val webCrawlerSatoriReaderProvider: WebCrawlerSatoriReaderProvider,
    private val seleniumSatoriReaderProvider: SeleniumSatoriReaderProvider
) {

    fun locate(request: SatoriReaderProviderLocatorRequest): SatoriReaderProvider {
        if (request.quiet)
            return webCrawlerSatoriReaderProvider

        return seleniumSatoriReaderProvider
    }
}

package com.copetti.srcli.provider.satori

import com.copetti.srcli.domain.gateway.SatoriReaderProvider
import com.copetti.srcli.domain.model.*
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker

class ThrottledSatoriReaderProvider(
    private val satoriReaderProvider: SatoriReaderProvider,
    minimumDelayMs: Long = DEFAULT_MIN_DELAY_MS
) : SatoriReaderProvider {

    @OptIn(ObsoleteCoroutinesApi::class)
    private val throttler = ticker(delayMillis = minimumDelayMs)
    override suspend fun login(request: SatoriReaderCredentials): SatoriReaderLoginToken {
        throttler.receive()
        return satoriReaderProvider.login(request)
    }

    override suspend fun fetchSeries(): List<SatoriReaderSeriesReference> {
        throttler.receive()
        return satoriReaderProvider.fetchSeries()
    }

    override suspend fun fetchSeriesContent(request: FetchSeriesContentRequest): SatoriReaderSeriesContent {
        throttler.receive()
        return satoriReaderProvider.fetchSeriesContent(request)
    }

    override suspend fun resetReadingProgress(request: ResetEditionReadingProgressRequest) {
        throttler.receive()
        return satoriReaderProvider.resetReadingProgress(request)
    }

    companion object {
        private const val DEFAULT_MIN_DELAY_MS: Long = 300
    }

}
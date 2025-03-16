package com.copetti.core.usecase

import com.copetti.core.gateway.SatoriReaderProvider
import com.copetti.model.*


data class FetchAllContentRequest(
    val credentials: SatoriReaderCredentials
)

class FetchAllContent(
    private val satoriReaderProvider: SatoriReaderProvider
) {

    fun fetchAllContent(request: FetchAllContentRequest): List<SatoriReaderSeriesContent> {
        val token = satoriReaderProvider.login(request.credentials)
        return satoriReaderProvider.fetchSeries()
            .map { series -> fetchSeriesContent(token, series) }
    }

    private fun fetchSeriesContent(token: SatoriReaderLoginToken, series: SatoriReaderSeries) =
        satoriReaderProvider.fetchSeriesContent(
            FetchSeriesContentRequest(token = token, series = series)
        )
}
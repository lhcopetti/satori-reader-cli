package com.copetti.core.usecase

import com.copetti.core.gateway.SatoriReaderProvider
import com.copetti.model.*


data class FetchAllSatoriReaderContentRequest(
    val credentials: SatoriReaderCredentials
)

class FetchAllSatoriReaderContent(
    private val satoriReaderProvider: SatoriReaderProvider
) {

    fun fetchAllContent(request: FetchAllSatoriReaderContentRequest): List<SatoriReaderSeriesContent> {
        val token = satoriReaderProvider.login(request.credentials)
        return satoriReaderProvider.fetchSeries()
            .map { series -> fetchSeriesContent(token, series) }
    }

    private fun fetchSeriesContent(token: SatoriReaderLoginToken, series: SatoriReaderSeriesReference) =
        satoriReaderProvider.fetchSeriesContent(
            FetchSeriesContentRequest(token = token, series = series)
        )
}
package com.copetti.core.usecase

import com.copetti.core.gateway.SatoriReaderProvider
import com.copetti.model.FetchSeriesContentRequest
import com.copetti.model.SatoriReaderLoginToken
import com.copetti.model.SatoriReaderSeriesContent
import com.copetti.model.SatoriReaderSeriesReference


data class FetchAllSatoriReaderContentRequest(
    val token: SatoriReaderLoginToken
)

class FetchAllSatoriReaderContent(
    private val satoriReaderProvider: SatoriReaderProvider
) {

    fun fetch(request: FetchAllSatoriReaderContentRequest): List<SatoriReaderSeriesContent> {
        return satoriReaderProvider.fetchSeries()
            .map { series -> fetchSeriesContent(request, series) }
    }

    private fun fetchSeriesContent(request: FetchAllSatoriReaderContentRequest, series: SatoriReaderSeriesReference) =
        satoriReaderProvider.fetchSeriesContent(
            FetchSeriesContentRequest(token = request.token, series = series)
        )
}
package com.copetti.srcli.domain.usecase

import com.copetti.srcli.domain.gateway.SatoriReaderProvider
import com.copetti.srcli.domain.model.FetchSeriesContentRequest
import com.copetti.srcli.domain.model.SatoriReaderLoginToken
import com.copetti.srcli.domain.model.SatoriReaderSeriesContent
import com.copetti.srcli.domain.model.SatoriReaderSeriesReference


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
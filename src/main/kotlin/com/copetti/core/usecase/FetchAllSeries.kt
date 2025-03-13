package com.copetti.core.usecase

import com.copetti.core.gateway.SatoriReaderProvider
import com.copetti.model.SatoriReaderCredentials


data class FetchAllSeriesRequest(
    val credentials: SatoriReaderCredentials
)

class FetchAllSeries(
    private val satoriReaderProvider: SatoriReaderProvider
) {

    fun fetch(request: ResetReadingProgressRequest) {
        satoriReaderProvider.fetchAllSeries(FetchAllSeriesRequest(credentials = request.credentials))
    }
}
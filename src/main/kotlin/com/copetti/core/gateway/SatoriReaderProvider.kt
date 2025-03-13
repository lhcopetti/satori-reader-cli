package com.copetti.core.gateway

import com.copetti.core.usecase.FetchAllSeriesRequest
import com.copetti.core.usecase.ResetReadingProgressRequest
import com.copetti.model.SatoriReaderSeries


interface SatoriReaderProvider {

    fun fetchAllSeries(request: FetchAllSeriesRequest): List<SatoriReaderSeries>

    fun resetReadingProgress(request: ResetReadingProgressRequest)

}
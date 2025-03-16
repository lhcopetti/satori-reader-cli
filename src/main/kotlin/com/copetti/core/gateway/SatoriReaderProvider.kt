package com.copetti.core.gateway

import com.copetti.core.usecase.FetchAllSeriesRequest
import com.copetti.core.usecase.ResetReadingProgressRequest
import com.copetti.model.*


interface SatoriReaderProvider {

    fun login(request: SatoriReaderCredentials): SatoriReaderLoginToken

    fun fetchSeries(): List<SatoriReaderSeries>

    fun fetchAllSeries(request: FetchAllSeriesRequest): List<SatoriReaderSeriesContent>

    fun resetReadingProgress(request: ResetReadingProgressRequest)

}
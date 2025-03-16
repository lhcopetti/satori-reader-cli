package com.copetti.core.gateway

import com.copetti.core.usecase.ResetReadingProgressRequest
import com.copetti.model.*


interface SatoriReaderProvider {

    fun login(request: SatoriReaderCredentials): SatoriReaderLoginToken

    fun fetchSeries(): List<SatoriReaderSeriesReference>

    fun fetchSeriesContent(request: FetchSeriesContentRequest): SatoriReaderSeriesContent

    fun resetReadingProgress(request: ResetReadingProgressRequest)

}
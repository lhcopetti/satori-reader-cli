package com.copetti.srcli.domain.gateway

import com.copetti.srcli.domain.model.*


interface SatoriReaderProvider {

    fun login(request: SatoriReaderCredentials): SatoriReaderLoginToken

    fun fetchSeries(): List<SatoriReaderSeriesReference>

    fun fetchSeriesContent(request: FetchSeriesContentRequest): SatoriReaderSeriesContent

    fun resetReadingProgress(request: ResetEditionReadingProgressRequest)

}
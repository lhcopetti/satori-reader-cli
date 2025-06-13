package com.copetti.srcli.domain.gateway

import com.copetti.srcli.domain.model.*


interface SatoriReaderProvider {

    suspend fun login(request: LoginApplicationCredentials): SatoriReaderLoginToken

    suspend fun fetchSeries(): List<SatoriReaderSeriesReference>

    suspend fun fetchSeriesContent(request: FetchSeriesContentRequest): SatoriReaderSeriesContent

    suspend fun resetReadingProgress(request: ResetEditionReadingProgressRequest)

}
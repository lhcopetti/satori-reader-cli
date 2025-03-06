package com.copetti.core.gateway

import com.copetti.model.SatoriReaderSeries


data class SatoriReaderCredentials(
    val username: String,
    val password: String
)

data class SatoriReaderProviderRequest(
    val credentials: SatoriReaderCredentials
)

interface SatoriReaderProvider {

    fun fetchAllSeries(request: SatoriReaderProviderRequest): List<SatoriReaderSeries>

    fun resetReadingProgress(request: SatoriReaderProviderRequest)

}
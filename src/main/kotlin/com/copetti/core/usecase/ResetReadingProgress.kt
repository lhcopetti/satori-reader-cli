package com.copetti.core.usecase

import com.copetti.core.gateway.SatoriReaderCredentials
import com.copetti.core.gateway.SatoriReaderProviderRequest
import com.copetti.provider.satori.SatoriReaderProviderLocator
import com.copetti.provider.satori.SatoriReaderProviderLocatorRequest


data class ResetReadingProgressRequest(
    val credentials: SatoriReaderCredentials,
    val quiet: Boolean
)

class ResetReadingProgress(
    private val satoriReaderProviderLocator: SatoriReaderProviderLocator
) {

    fun reset(request: ResetReadingProgressRequest) {
        val locateRequest = SatoriReaderProviderLocatorRequest(quiet = request.quiet)
        satoriReaderProviderLocator.locate(locateRequest)
            .resetReadingProgress(SatoriReaderProviderRequest(credentials = request.credentials))
    }
}
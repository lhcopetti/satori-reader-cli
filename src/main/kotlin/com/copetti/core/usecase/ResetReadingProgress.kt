package com.copetti.core.usecase

import com.copetti.core.gateway.SatoriReaderCredentials
import com.copetti.core.gateway.SatoriReaderProvider
import com.copetti.core.gateway.SatoriReaderProviderRequest


data class ResetReadingProgressRequest(
    val credentials: SatoriReaderCredentials
)

class ResetReadingProgress(
    private val satoriReaderProvider: SatoriReaderProvider
) {

    fun reset(request: ResetReadingProgressRequest) {
        satoriReaderProvider.resetReadingProgress(SatoriReaderProviderRequest(credentials = request.credentials))
    }
}
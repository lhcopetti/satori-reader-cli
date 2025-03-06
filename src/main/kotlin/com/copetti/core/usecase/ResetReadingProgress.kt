package com.copetti.core.usecase

import com.copetti.core.gateway.SatoriReaderCredentials
import com.copetti.core.gateway.SatoriReaderProvider
import com.copetti.core.gateway.SatoriReaderProviderRequest


data class ResetReadingProgressRequest(
    val credentials: SatoriReaderCredentials
)

class ResetReadingProgress(
    private val repository: SatoriReaderProvider
) {

    fun reset(request: ResetReadingProgressRequest) {
        val providerRequest = SatoriReaderProviderRequest(credentials = request.credentials)
        repository.resetReadingProgress(providerRequest)
    }
}
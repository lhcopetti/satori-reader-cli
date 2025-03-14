package com.copetti.core.usecase

import com.copetti.core.gateway.SatoriReaderProvider
import com.copetti.model.SatoriReaderCredentials


data class ResetReadingProgressRequest(
    val credentials: SatoriReaderCredentials
)

class ResetReadingProgress(
    private val satoriReaderProvider: SatoriReaderProvider
) {

    fun reset(request: ResetReadingProgressRequest) {
        satoriReaderProvider.resetReadingProgress(ResetReadingProgressRequest(credentials = request.credentials))
    }
}
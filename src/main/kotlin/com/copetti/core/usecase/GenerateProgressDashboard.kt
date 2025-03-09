package com.copetti.core.usecase

import com.copetti.core.gateway.SatoriReaderCredentials

data class GenerateProgressDashboardRequest(
    val credentials: SatoriReaderCredentials
)

class GenerateProgressDashboard(
    private val retrieveReadingProgress: RetrieveReadingProgress,
    private val buildProgressDashboard: BuildProgressDashboard
) {

    fun generate(request: GenerateProgressDashboardRequest): String {
        val progression = retrieveReadingProgress.retrieve(
            RetrieveReadingProgressRequest(credentials = request.credentials)
        )
        return buildProgressDashboard.build(
            BuildProgressDashboardRequest(progression)
        )
    }
}
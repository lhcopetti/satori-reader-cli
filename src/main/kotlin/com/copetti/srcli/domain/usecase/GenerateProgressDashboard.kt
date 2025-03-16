package com.copetti.srcli.domain.usecase

import com.copetti.srcli.domain.model.SatoriReaderCredentials

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
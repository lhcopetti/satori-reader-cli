package com.copetti.srcli.domain.usecase.progress

import com.copetti.srcli.domain.model.ApplicationCredentials

data class GenerateProgressDashboardRequest(
    val credentials: ApplicationCredentials
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
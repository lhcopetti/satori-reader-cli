package com.copetti.cli

import com.copetti.core.gateway.SatoriReaderCredentials
import com.copetti.core.usecase.GenerateProgressDashboard
import com.copetti.core.usecase.GenerateProgressDashboardRequest
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject

class GenerateProgressDashboardCommand(
    private val generateProgressDashboard: GenerateProgressDashboard
) : CliktCommand() {

    private val config by requireObject<SatoriReaderCliContext>()

    override fun run() {
        val username = config.credentials.username
        val password = config.credentials.password
        val request = GenerateProgressDashboardRequest(
            credentials = SatoriReaderCredentials(username = username, password = password)
        )
        generateProgressDashboard.generate(request)
    }
}
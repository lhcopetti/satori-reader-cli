package com.copetti.cli

import com.copetti.core.gateway.SatoriReaderCredentials
import com.copetti.core.usecase.UpdateReadme
import com.copetti.core.usecase.UpdateReadmeRequest
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject

class GenerateProgressDashboardCommand(
    private val updateReadme: UpdateReadme
) : CliktCommand() {

    private val config by requireObject<SatoriReaderCliContext>()

    override fun run() {
        val username = config.credentials.username
        val password = config.credentials.password
        val request = UpdateReadmeRequest(
            credentials = SatoriReaderCredentials(username = username, password = password)
        )
        updateReadme.update(request)
    }
}
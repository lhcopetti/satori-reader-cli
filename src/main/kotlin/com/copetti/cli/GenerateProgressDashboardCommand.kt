package com.copetti.cli

import com.copetti.core.usecase.UpdateReadme
import com.copetti.core.usecase.UpdateReadmeRequest
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject

class GenerateProgressDashboardCommand(
    private val updateReadme: UpdateReadme
) : CliktCommand() {

    private val config by requireObject<SatoriReaderCliContext>()

    override fun run() {
        val request = UpdateReadmeRequest(credentials = config.credentials)
        updateReadme.update(request)
    }
}
package com.copetti.cli

import com.copetti.core.model.SatoriCredentials
import com.copetti.core.usecase.ResetReadingProgress
import com.copetti.core.usecase.ResetReadingProgressRequest
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.provideDelegate

class ResetReadingProgressCommand(
    private val resetReadingProgress: ResetReadingProgress
) : CliktCommand() {

    private val baseOptions by BaseOptions()
    override fun run() {
        val username = baseOptions.username
        val password = baseOptions.password
        val request = ResetReadingProgressRequest(
            credentials = SatoriCredentials(login = username, password = password)
        )
        resetReadingProgress.reset(request)
    }
}
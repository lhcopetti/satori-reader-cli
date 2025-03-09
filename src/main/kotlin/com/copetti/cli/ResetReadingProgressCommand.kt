package com.copetti.cli

import com.copetti.core.gateway.SatoriReaderCredentials
import com.copetti.core.usecase.ResetReadingProgress
import com.copetti.core.usecase.ResetReadingProgressRequest
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject

class ResetReadingProgressCommand(
    private val resetReadingProgress: ResetReadingProgress
) : CliktCommand() {

    private val config by requireObject<SatoriReaderCliContext>()

    override fun run() {
        val username = config.credentials.username
        val password = config.credentials.password
        val request = ResetReadingProgressRequest(
            credentials = SatoriReaderCredentials(username = username, password = password),
            quiet = config.quiet
        )
        resetReadingProgress.reset(request)
    }
}
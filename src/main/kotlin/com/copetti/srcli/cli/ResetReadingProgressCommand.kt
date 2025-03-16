package com.copetti.srcli.cli

import com.copetti.srcli.domain.usecase.cli.ResetReadingProgress
import com.copetti.srcli.domain.usecase.cli.ResetReadingProgressRequest
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject

class ResetReadingProgressCommand(
    private val resetReadingProgress: ResetReadingProgress
) : CliktCommand() {

    private val config by requireObject<SatoriReaderCliContext>()

    override fun run() {
        val request = ResetReadingProgressRequest(credentials = config.credentials)
        resetReadingProgress.reset(request)
    }
}
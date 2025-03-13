package com.copetti.cli

import com.copetti.core.usecase.ResetReadingProgress
import com.copetti.core.usecase.ResetReadingProgressRequest
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
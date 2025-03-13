package com.copetti.cli

import com.copetti.core.usecase.UpdateReadmeProgress
import com.copetti.core.usecase.UpdateReadmeRequest
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject

class UpdateReadmeProgressCommand(
    private val updateReadmeProgress: UpdateReadmeProgress
) : CliktCommand() {

    private val config by requireObject<SatoriReaderCliContext>()

    override fun run() {
        val request = UpdateReadmeRequest(credentials = config.credentials)
        updateReadmeProgress.update(request)
    }
}
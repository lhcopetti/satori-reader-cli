package com.copetti.srcli.cli

import com.copetti.srcli.domain.model.ApplicationCredentials
import com.copetti.srcli.domain.model.LoginApplicationCredentials
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.options.required

data class ApplicationCliContext(
    val credentials: ApplicationCredentials
)

class SatoriReaderCliCommand : CliktCommand() {

    private val username: String by option().required()
    private val password: String by option().prompt(hideInput = true)

    override fun run() {
        currentContext.findOrSetObject {
            ApplicationCliContext(
                credentials = LoginApplicationCredentials(
                    username = username,
                    password = password
                )
            )
        }
    }
}
package com.copetti.cli

import com.copetti.core.gateway.SatoriReaderCredentials
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.options.required

data class SatoriReaderCliContext(
    val credentials: SatoriReaderCredentials,
    val quiet: Boolean
)

class SatoriReaderCliCommand : CliktCommand() {

    private val username: String by option().required()
    private val password: String by option().prompt(hideInput = true)
    private val boring by option().flag()

    override fun run() {
        currentContext.findOrSetObject {
            SatoriReaderCliContext(
                credentials = SatoriReaderCredentials(
                    username = username,
                    password = password
                ),
                quiet = boring
            )
        }
    }
}
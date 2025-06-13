package com.copetti.srcli.cli

import com.copetti.srcli.domain.model.ApplicationCredentials
import com.copetti.srcli.domain.model.LoginApplicationCredentials
import com.copetti.srcli.domain.model.TokenApplicationCredentials
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.groups.groupChoice
import com.github.ajalt.clikt.parameters.groups.required
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.options.required

data class ApplicationCliContext(
    val credentials: ApplicationCredentials
)

sealed class AuthCredentialsOptionGroup : OptionGroup()
class LoginCredentialsOptionGroup : AuthCredentialsOptionGroup() {

    val password: String by option().prompt(hideInput = true)
    val username: String by option().required()
}

class TokenCredentialsOptionGroup : AuthCredentialsOptionGroup() {
    val token: String by option().prompt(hideInput = true)
}

class SatoriReaderCliCommand : CliktCommand() {

    private val auth by option().groupChoice(
        "login" to LoginCredentialsOptionGroup(),
        "token" to TokenCredentialsOptionGroup()
    ).required()

    override fun run() {
        currentContext.findOrSetObject {
            val credentials = when (val it = auth) {
                is LoginCredentialsOptionGroup -> LoginApplicationCredentials(
                    username = it.username,
                    password = it.password
                )

                is TokenCredentialsOptionGroup -> TokenApplicationCredentials(token = it.token)
            }
            ApplicationCliContext(credentials = credentials)
        }
    }
}
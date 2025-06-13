package com.copetti.srcli.domain.usecase.cli

import com.copetti.srcli.domain.gateway.SatoriReaderProvider
import com.copetti.srcli.domain.model.ApplicationCredentials
import com.copetti.srcli.domain.model.LoginApplicationCredentials
import com.copetti.srcli.domain.model.SatoriReaderLoginToken
import com.copetti.srcli.domain.model.TokenApplicationCredentials

class AuthenticateUser(
    private val satoriReaderProvider: SatoriReaderProvider,
) {

    suspend fun authenticate(credentials: ApplicationCredentials): SatoriReaderLoginToken {
        return when (credentials) {
            is LoginApplicationCredentials -> satoriReaderProvider.login(credentials)
            is TokenApplicationCredentials -> SatoriReaderLoginToken(credentials.token)
        }
    }
}
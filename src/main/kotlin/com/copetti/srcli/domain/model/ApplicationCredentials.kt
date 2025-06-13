package com.copetti.srcli.domain.model

data class LoginApplicationCredentials(
    val username: String,
    val password: String
) : ApplicationCredentials

data class TokenApplicationCredentials(
    val token: String
) : ApplicationCredentials

sealed interface ApplicationCredentials
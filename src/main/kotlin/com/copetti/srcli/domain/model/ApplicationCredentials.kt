package com.copetti.srcli.domain.model

data class LoginApplicationCredentials(
    val username: String,
    val password: String
) : ApplicationCredentials

sealed interface ApplicationCredentials
package com.copetti.core.usecase

import com.copetti.core.SatoriReaderRepository
import com.copetti.core.SatoriReaderRepositoryRequest
import com.copetti.core.model.SatoriCredentials


data class ResetReadingProgressRequest(
    val credentials: SatoriCredentials
)

class ResetReadingProgress(
    private val repository: SatoriReaderRepository
) {


    fun reset(request: ResetReadingProgressRequest) {
        val loginRequest = SatoriReaderRepositoryRequest(
            login = request.credentials.login,
            password = request.credentials.password
        )
        repository.login(loginRequest)
        repository.resetReadingProgress()
    }
}
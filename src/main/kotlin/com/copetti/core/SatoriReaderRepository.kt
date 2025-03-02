package com.copetti.core

import com.copetti.model.SatoriReaderSeries

data class SatoriReaderRepositoryRequest(
    val login: String,
    val password: String
)

interface SatoriReaderRepository {

    fun login(request: SatoriReaderRepositoryRequest)

    fun fetchAllSeries(request: SatoriReaderRepositoryRequest): List<SatoriReaderSeries>

    fun resetProgress(episodes: List<SatoriReaderSeries>)
}
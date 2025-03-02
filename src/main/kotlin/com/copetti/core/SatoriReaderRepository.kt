package com.copetti.core

import com.copetti.model.SatoriReaderSeries

data class SatoriReaderRepositoryRequest(
    val login: String,
    val password: String
)

interface SatoriReaderRepository {

    fun fetchAllSeries(request: SatoriReaderRepositoryRequest): List<SatoriReaderSeries>
}
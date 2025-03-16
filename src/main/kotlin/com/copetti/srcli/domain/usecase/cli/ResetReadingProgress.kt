package com.copetti.srcli.domain.usecase.cli

import com.copetti.srcli.domain.gateway.SatoriReaderProvider
import com.copetti.srcli.domain.model.*
import com.copetti.srcli.domain.usecase.FetchAllSatoriReaderContent
import com.copetti.srcli.domain.usecase.FetchAllSatoriReaderContentRequest


data class ResetReadingProgressRequest(
    val credentials: SatoriReaderCredentials
)

class ResetReadingProgress(
    private val satoriReaderProvider: SatoriReaderProvider,
    private val fetchAllSatoriReaderContent: FetchAllSatoriReaderContent
) {

    fun reset(request: ResetReadingProgressRequest) {
        val token = satoriReaderProvider.login(request.credentials)
        val retrieveRequest = FetchAllSatoriReaderContentRequest(token = token)
        val allSeries = fetchAllSatoriReaderContent.fetch(retrieveRequest)

        allSeries
            .flatMap(SatoriReaderSeriesContent::episodes)
            .flatMap(SatoriReaderEpisode::editions)
            .filter { edition -> edition.status != SatoriReaderStatus.UNREAD }
            .forEach { edition -> resetProgress(token, edition) }
    }

    private fun resetProgress(token: SatoriReaderLoginToken, edition: SatoriReaderEdition) {
        satoriReaderProvider.resetReadingProgress(ResetEditionReadingProgressRequest(token = token, edition = edition))
    }

}
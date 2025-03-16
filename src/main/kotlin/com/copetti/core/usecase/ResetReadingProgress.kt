package com.copetti.core.usecase

import com.copetti.core.gateway.SatoriReaderProvider
import com.copetti.model.*


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
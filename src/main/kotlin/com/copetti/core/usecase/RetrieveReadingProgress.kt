package com.copetti.core.usecase

import com.copetti.core.gateway.SatoriReaderProvider
import com.copetti.model.SatoriReaderCredentials
import com.copetti.model.SatoriReaderStatus

data class SeriesProgression(
    val title: String,
    val episodes: List<EpisodeProgression>
)

data class EpisodeProgression(
    val title: String,
    val status: SatoriReaderStatus
)

data class RetrieveReadingProgressRequest(
    val credentials: SatoriReaderCredentials
)

class RetrieveReadingProgress(
    private val satoriReaderProvider: SatoriReaderProvider,
    private val retrieveAllSatoriReaderSeries: RetrieveAllSatoriReaderSeries
) {

    fun retrieve(request: RetrieveReadingProgressRequest): List<SeriesProgression> {
        val token = satoriReaderProvider.login(request.credentials)
        val providerRequest = RetrieveAllSatoriReaderSeriesRequest(token = token)
        val allSeries = retrieveAllSatoriReaderSeries.retrieve(providerRequest)
        return allSeries.map { series ->
            SeriesProgression(
                title = series.title,
                episodes = series.episodes.map { episode ->
                    EpisodeProgression(
                        title = episode.title,
                        status = episode.edition.status
                    )
                }
            )
        }
    }
}
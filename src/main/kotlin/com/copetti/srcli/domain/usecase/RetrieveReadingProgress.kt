package com.copetti.srcli.domain.usecase

import com.copetti.srcli.domain.gateway.SatoriReaderProvider
import com.copetti.srcli.domain.model.SatoriReaderCredentials
import com.copetti.srcli.domain.model.SatoriReaderStatus
import kotlinx.coroutines.runBlocking

data class SeriesProgression(
    val title: String,
    val link: String,
    val episodes: List<EpisodeProgression>
)

data class EpisodeProgression(
    val title: String,
    val status: SatoriReaderStatus,
    val link: String
)

data class RetrieveReadingProgressRequest(
    val credentials: SatoriReaderCredentials
)

class RetrieveReadingProgress(
    private val satoriReaderProvider: SatoriReaderProvider,
    private val retrieveAllSatoriReaderSeries: RetrieveAllSatoriReaderSeries
) {

    fun retrieve(request: RetrieveReadingProgressRequest): List<SeriesProgression> {
        return runBlocking {
            val token = satoriReaderProvider.login(request.credentials)
            val providerRequest = RetrieveAllSatoriReaderSeriesRequest(token = token)
            val allSeries = retrieveAllSatoriReaderSeries.retrieve(providerRequest)
            allSeries.map { series ->
                SeriesProgression(
                    title = series.title,
                    link = series.link,
                    episodes = series.episodes.map { episode ->
                        EpisodeProgression(
                            title = episode.title,
                            link = episode.edition.link,
                            status = episode.edition.status,
                        )
                    }
                )
            }
        }
    }
}
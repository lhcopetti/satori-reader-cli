package com.copetti.core.usecase

import com.copetti.core.gateway.SatoriReaderProvider
import com.copetti.model.SatoriReaderCredentials
import com.copetti.model.SatoriReaderEpisode
import com.copetti.model.SatoriReaderSeriesContent
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
    private val selectPrimaryEdition: SelectPrimaryEdition,
    private val satoriReaderProvider: SatoriReaderProvider
) {

    fun retrieve(request: RetrieveReadingProgressRequest): List<SeriesProgression> {
        val providerRequest = FetchAllSeriesRequest(credentials = request.credentials)
        val allSeries = satoriReaderProvider.fetchAllSeries(providerRequest)
        return allSeries.map(this::mapSeries)
    }

    private fun mapSeries(series: SatoriReaderSeriesContent): SeriesProgression {
        return SeriesProgression(
            title = series.title,
            episodes = series.episodes.map(this::mapEpisode)
        )
    }

    private fun mapEpisode(episode: SatoriReaderEpisode): EpisodeProgression {
        val primaryEdition = selectPrimaryEdition.select(episode)
        return EpisodeProgression(
            title = episode.title,
            status = primaryEdition.status
        )
    }
}
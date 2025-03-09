package com.copetti.core.usecase

import com.copetti.core.gateway.SatoriReaderCredentials
import com.copetti.core.gateway.SatoriReaderProviderRequest
import com.copetti.model.SatoriReaderEpisode
import com.copetti.model.SatoriReaderSeries
import com.copetti.model.SatoriReaderStatus
import com.copetti.provider.satori.SatoriReaderProviderLocator
import com.copetti.provider.satori.SatoriReaderProviderLocatorRequest

data class RetrieveReadingProgressRequest(
    val credentials: SatoriReaderCredentials,
    val quiet: Boolean
) {
}

data class SeriesProgression(
    val title: String,
    val episodes: List<EpisodeProgression>
)

data class EpisodeProgression(
    val title: String,
    val status: SatoriReaderStatus
)

class RetrieveReadingProgress(
    private val selectPrimaryEdition: SelectPrimaryEdition,
    private val satoriReaderProviderLocator: SatoriReaderProviderLocator
) {

    fun retrieve(request: RetrieveReadingProgressRequest): List<SeriesProgression> {
        val providerRequest = SatoriReaderProviderRequest(credentials = request.credentials)
        val allSeries = locate(request).fetchAllSeries(providerRequest)
        return allSeries.map(this::mapSeries)
    }

    private fun locate(request: RetrieveReadingProgressRequest) =
        satoriReaderProviderLocator.locate(SatoriReaderProviderLocatorRequest(quiet = request.quiet))

    private fun mapSeries(series: SatoriReaderSeries): SeriesProgression {
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
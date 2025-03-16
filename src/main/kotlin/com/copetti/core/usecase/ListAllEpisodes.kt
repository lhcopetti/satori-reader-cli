package com.copetti.core.usecase

import com.copetti.model.SatoriReaderCredentials
import com.copetti.model.SatoriReaderSeries
import com.copetti.model.SatoriReaderStatus

data class ListAllEpisodesRequest(
    val credentials: SatoriReaderCredentials
)

data class EpisodeStatus(
    val title: String,
    val edition: String,
    val status: SatoriReaderStatus,
    val link: String
)


class ListAllEpisodes(
    private val retrieveAllSatoriReaderSeries: RetrieveAllSatoriReaderSeries
) {

    fun list(request: ListAllEpisodesRequest): List<EpisodeStatus> {
        val providerRequest = RetrieveAllSatoriReaderSeriesRequest(credentials = request.credentials)
        val allSeries = retrieveAllSatoriReaderSeries.retrieve(providerRequest)
        return listAllEpisodes(allSeries)
    }

    private fun listAllEpisodes(allSeries: List<SatoriReaderSeries>): List<EpisodeStatus> {
        return allSeries.map { series ->
            series.episodes.map { episode ->
                EpisodeStatus(
                    title = series.title,
                    edition = episode.edition.name,
                    status = episode.edition.status,
                    link = episode.edition.url
                )
            }
        }
            .flatten()

    }
}

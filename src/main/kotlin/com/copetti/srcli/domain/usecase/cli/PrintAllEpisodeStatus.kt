package com.copetti.srcli.domain.usecase.cli

import com.copetti.srcli.domain.gateway.SatoriReaderProvider
import com.copetti.srcli.domain.model.SatoriReaderCredentials
import com.copetti.srcli.domain.model.SatoriReaderSeries
import com.copetti.srcli.domain.model.SatoriReaderStatus
import com.copetti.srcli.domain.usecase.RetrieveAllSatoriReaderSeries
import com.copetti.srcli.domain.usecase.RetrieveAllSatoriReaderSeriesRequest

data class ListAllEpisodesRequest(
    val credentials: SatoriReaderCredentials
)

data class EpisodeStatus(
    val title: String,
    val edition: String,
    val status: SatoriReaderStatus,
    val link: String
)


class PrintAllEpisodeStatus(
    private val satoriReaderProvider: SatoriReaderProvider,
    private val retrieveAllSatoriReaderSeries: RetrieveAllSatoriReaderSeries,
) {

    fun print(request: ListAllEpisodesRequest): String {
        val token = satoriReaderProvider.login(request.credentials)
        val providerRequest = RetrieveAllSatoriReaderSeriesRequest(token = token)
        val allSeries = retrieveAllSatoriReaderSeries.retrieve(providerRequest)
        return retrieveAllEpisodesStatus(allSeries).joinToString(separator = System.lineSeparator()) { episode ->
            "${episode.title},${episode.edition},${episode.status},${episode.link}"
        }
    }

    private fun retrieveAllEpisodesStatus(allSeries: List<SatoriReaderSeries>): List<EpisodeStatus> {
        return allSeries.map { series ->
            series.episodes.map { episode ->
                EpisodeStatus(
                    title = series.title,
                    edition = episode.edition.name,
                    status = episode.edition.status,
                    link = episode.edition.urlPath
                )
            }
        }
            .flatten()

    }
}

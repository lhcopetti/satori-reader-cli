package com.copetti.core.usecase

import com.copetti.model.*

data class RetrieveAllSatoriReaderSeriesRequest(
    val credentials: SatoriReaderCredentials
)

class RetrieveAllSatoriReaderSeries(
    private val fetchAllSatoriReaderContent: FetchAllSatoriReaderContent,
    private val selectPrimaryEdition: SelectPrimaryEdition
) {

    fun retrieve(request: RetrieveAllSatoriReaderSeriesRequest): List<SatoriReaderSeries> {
        val providerRequest = FetchAllSatoriReaderContentRequest(credentials = request.credentials)
        val allSeries = fetchAllSatoriReaderContent.fetchAllContent(providerRequest)
        return allSeries.map(this::mapSeries)
    }

    private fun mapSeries(series: SatoriReaderSeriesContent): SatoriReaderSeries {
        return SatoriReaderSeries(
            title = series.title,
            episodes = series.episodes.map(this::mapEpisode)
        )
    }

    private fun mapEpisode(episode: SatoriReaderEpisode): SatoriReaderPrimaryEditionEpisode {
        val primaryEdition = selectPrimaryEdition.select(episode)
        return SatoriReaderPrimaryEditionEpisode(
            title = episode.title,
            edition = primaryEdition
        )
    }
}
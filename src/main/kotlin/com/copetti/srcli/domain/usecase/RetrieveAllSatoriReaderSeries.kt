package com.copetti.srcli.domain.usecase

import com.copetti.srcli.domain.model.*

data class RetrieveAllSatoriReaderSeriesRequest(
    val token: SatoriReaderLoginToken
)

class RetrieveAllSatoriReaderSeries(
    private val fetchAllSatoriReaderContent: FetchAllSatoriReaderContent,
    private val selectPrimaryEdition: SelectPrimaryEdition
) {

    fun retrieve(request: RetrieveAllSatoriReaderSeriesRequest): List<SatoriReaderSeries> {
        val providerRequest = FetchAllSatoriReaderContentRequest(token = request.token)
        val allSeries = fetchAllSatoriReaderContent.fetch(providerRequest)
        return allSeries
            .map(this::mapSeries)
    }

    private fun mapSeries(series: SatoriReaderSeriesContent): SatoriReaderSeries {
        return SatoriReaderSeries(
            title = series.title,
            link = series.link,
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
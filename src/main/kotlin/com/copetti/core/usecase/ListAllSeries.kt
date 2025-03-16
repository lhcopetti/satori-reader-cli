package com.copetti.core.usecase

import com.copetti.core.gateway.SatoriReaderProvider
import com.copetti.model.SatoriReaderCredentials
import com.copetti.model.SatoriReaderEdition
import com.copetti.model.SatoriReaderSeriesContent

data class ListAllSeriesRequest(
    val credentials: SatoriReaderCredentials
)

data class SeriesWithRankedEditions(
    val title: String,
    val editions: List<SatoriReaderEdition>,
)


class ListAllSeries(
    private val provider: SatoriReaderProvider,
    private val selectPrimaryEdition: SelectPrimaryEdition
) {

    fun list(request: ListAllSeriesRequest): List<SeriesWithRankedEditions> {
        val providerRequest = FetchAllSeriesRequest(credentials = request.credentials)
        val allSeries = provider.fetchAllSeries(providerRequest)
        return listAllEpisodes(allSeries)
    }

    private fun listAllEpisodes(satoriReaderSeryWithEpisodes: List<SatoriReaderSeriesContent>): List<SeriesWithRankedEditions> {
        val allSeries = mutableListOf<SeriesWithRankedEditions>()
        for (series in satoriReaderSeryWithEpisodes) {
            val editions = mutableListOf<SatoriReaderEdition>()
            for (episode in series.episodes) {
                val selectedEdition = selectPrimaryEdition.select(episode)
                editions.add(selectedEdition)
            }
            val seriesWithRankedEditions = SeriesWithRankedEditions(
                title = series.title,
                editions = editions
            )
            allSeries.add(seriesWithRankedEditions)
        }
        return allSeries
    }
}

package com.copetti.core.usecase

import com.copetti.core.gateway.SatoriReaderCredentials
import com.copetti.core.gateway.SatoriReaderProvider
import com.copetti.core.gateway.SatoriReaderProviderRequest
import com.copetti.model.SatoriReaderEdition
import com.copetti.model.SatoriReaderSeries
import com.copetti.model.SatoriReaderStatus
import java.util.*

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
    private val satoriReaderProvider: SatoriReaderProvider
) {

    fun print(request: ListAllEpisodesRequest): List<EpisodeStatus> {
        val providerRequest = SatoriReaderProviderRequest(credentials = request.credentials)
        val allSeries = satoriReaderProvider.fetchAllSeries(providerRequest)
        return listAllEpisodes(allSeries)
    }

    private fun listAllEpisodes(allSeries: List<SatoriReaderSeries>): List<EpisodeStatus> {
        val episodes = mutableListOf<EpisodeStatus>()
        for (series in allSeries) {
            for (episode in series.episodes) {
                val editionsByName = episode.editions.groupBy(SatoriReaderEdition::name)
                val selectedEdition: SatoriReaderEdition = RANKED_EDITIONS
                    .map { rankedEdition -> editionsByName[rankedEdition] }
                    .first(Objects::nonNull)
                    ?.first()
                    ?: throw IllegalStateException("Could not find a ranked edition")

                val episodeStatus = EpisodeStatus(
                    title = series.title,
                    edition = selectedEdition.name,
                    status = selectedEdition.status,
                    link = selectedEdition.url
                )
                episodes.add(episodeStatus)
            }

        }
        return episodes
    }

    companion object {
        private val RANKED_EDITIONS = listOf(
            "HARDER (SFX)",
            "HARDER",
            "HARDER (VOICE ONLY)",
            "WITH SOUND EFFECTS",
            "STANDARD",
            "NO SPACES",
            "NO KATAKANA",
            "MEDIUM",
            "EASIER",
            "EASIER (SFX)",
            "EASIER (VOICE ONLY)",
            "VOICE ONLY",
            "WITH SPACES (20 MINUTES)",
            "WITH SPACES (10 MINUTES)",
        )
    }
}

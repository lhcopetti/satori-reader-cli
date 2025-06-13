package com.copetti.srcli.domain.usecase.progress

import com.copetti.srcli.domain.model.ApplicationCredentials
import com.copetti.srcli.domain.model.SatoriReaderStatus
import com.copetti.srcli.domain.usecase.RetrieveAllSatoriReaderSeries
import com.copetti.srcli.domain.usecase.RetrieveAllSatoriReaderSeriesRequest
import com.copetti.srcli.domain.usecase.cli.AuthenticateUser
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
    val credentials: ApplicationCredentials
)

class RetrieveReadingProgress(
    private val authenticateUser: AuthenticateUser,
    private val retrieveAllSatoriReaderSeries: RetrieveAllSatoriReaderSeries
) {

    fun retrieve(request: RetrieveReadingProgressRequest): List<SeriesProgression> {
        return runBlocking {
            val token = authenticateUser.authenticate(request.credentials)
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
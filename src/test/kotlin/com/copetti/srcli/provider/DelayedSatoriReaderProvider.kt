package com.copetti.srcli.provider

import com.copetti.srcli.domain.gateway.SatoriReaderProvider
import com.copetti.srcli.domain.model.*
import kotlinx.coroutines.delay

class DelayedSatoriReaderProvider(
    private val numberOfEpisodes: Int,
    private val numberOfEditionsPerEpisode: Int,
    private val delayMs: Long
) : SatoriReaderProvider {

    override suspend fun login(request: LoginApplicationCredentials): SatoriReaderLoginToken {
        delay(delayMs)
        return SatoriReaderLoginToken(sessionToken = "")
    }

    override suspend fun fetchSeries(): List<SatoriReaderSeriesReference> {
        delay(delayMs)
        return (1..10).map { SatoriReaderSeriesReference("") }
    }

    override suspend fun fetchSeriesContent(request: FetchSeriesContentRequest): SatoriReaderSeriesContent {
        delay(delayMs)
        return SatoriReaderSeriesContent(title = "", link = "", episodes = createEpisodes())
    }

    override suspend fun resetReadingProgress(request: ResetEditionReadingProgressRequest) {
        delay(delayMs)
    }

    private fun createEpisodes(): List<SatoriReaderEpisode> {
        val editions: List<SatoriReaderEdition> = (1..numberOfEditionsPerEpisode).map {
            SatoriReaderEdition(
                name = "",
                urlPath = "",
                link = "",
                status = SatoriReaderStatus.COMPLETED
            )
        }
        return (1..numberOfEpisodes).map { SatoriReaderEpisode(title = "A:episode 1", editions = editions) }
    }
}
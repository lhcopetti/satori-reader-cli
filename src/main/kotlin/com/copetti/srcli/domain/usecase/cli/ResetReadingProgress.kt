package com.copetti.srcli.domain.usecase.cli

import com.copetti.srcli.domain.gateway.SatoriReaderProvider
import com.copetti.srcli.domain.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


data class ResetReadingProgressRequest(
    val credentials: ApplicationCredentials
)

class ResetReadingProgress(
    private val authenticateUser: AuthenticateUser,
    private val satoriReaderProvider: SatoriReaderProvider
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun reset(request: ResetReadingProgressRequest) = runBlocking {
        val token = authenticateUser.authenticate(request.credentials)
        val allSeries = satoriReaderProvider.fetchSeries()

        val unreadEditionsChannel = produce {
            allSeries.forEach { reference ->
                val unreadEditions = fetchUnreadEditionsFromSeriesReference(token, reference)
                unreadEditions.forEach { edition -> send(edition) }
            }
        }

        repeat(MAX_RESET_PROCESSOR_COUNT) {
            launch {
                for (edition in unreadEditionsChannel) {
                    resetEditionProgress(token, edition)
                }
            }
        }
    }

    private suspend fun fetchUnreadEditionsFromSeriesReference(
        token: SatoriReaderLoginToken,
        reference: SatoriReaderSeriesReference
    ): List<SatoriReaderEdition> {
        val fetchRequest = FetchSeriesContentRequest(token = token, series = reference)
        return satoriReaderProvider.fetchSeriesContent(fetchRequest)
            .episodes
            .flatMap(SatoriReaderEpisode::editions)
            .filter { edition -> edition.status != SatoriReaderStatus.UNREAD }
    }

    private suspend fun resetEditionProgress(token: SatoriReaderLoginToken, edition: SatoriReaderEdition) {
        satoriReaderProvider.resetReadingProgress(
            ResetEditionReadingProgressRequest(
                token = token,
                edition = edition
            )
        )
    }

    companion object {
        private const val MAX_RESET_PROCESSOR_COUNT = 5
    }

}
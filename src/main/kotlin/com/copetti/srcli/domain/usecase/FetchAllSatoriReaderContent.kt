package com.copetti.srcli.domain.usecase

import com.copetti.srcli.domain.gateway.SatoriReaderProvider
import com.copetti.srcli.domain.model.FetchSeriesContentRequest
import com.copetti.srcli.domain.model.SatoriReaderLoginToken
import com.copetti.srcli.domain.model.SatoriReaderSeriesReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking


data class FetchAllSatoriReaderContentRequest(
    val token: SatoriReaderLoginToken
)

class FetchAllSatoriReaderContent(
    private val satoriReaderProvider: SatoriReaderProvider
) {

    fun fetch(request: FetchAllSatoriReaderContentRequest) = runBlocking {
        satoriReaderProvider.fetchSeries()
            .map { reference -> fetchContentForSeries(request, reference) }
            .awaitAll()
    }

    private fun CoroutineScope.fetchContentForSeries(
        request: FetchAllSatoriReaderContentRequest,
        reference: SatoriReaderSeriesReference
    ) = async {
        val fetchContentRequest = FetchSeriesContentRequest(token = request.token, series = reference)
        satoriReaderProvider.fetchSeriesContent(fetchContentRequest)
    }

}
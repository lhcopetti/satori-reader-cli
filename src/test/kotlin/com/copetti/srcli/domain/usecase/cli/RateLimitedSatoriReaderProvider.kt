package com.copetti.srcli.domain.usecase.cli

import com.copetti.srcli.domain.gateway.SatoriReaderProvider
import com.copetti.srcli.domain.model.*
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

class RateLimitedSatoriReaderProvider : SatoriReaderProvider {

    private val rateLimitingBuffer: MutableList<Long> = mutableListOf()
    private val counter = AtomicInteger()

    override suspend fun login(request: LoginApplicationCredentials): SatoriReaderLoginToken {
        return SatoriReaderLoginToken(sessionToken = "")
    }

    override suspend fun fetchSeries(): List<SatoriReaderSeriesReference> {
        return (1..100).map {
            SatoriReaderSeriesReference(link = "link/$it")
        }
    }

    override suspend fun fetchSeriesContent(request: FetchSeriesContentRequest): SatoriReaderSeriesContent {
        println("FETCH_SERIES_CONTENT")
        verifyRateLimiting()
        increment()
        delay(Random.nextLong(10, 30))
        decrement()
        return createMockContent(episodeCount = 10, editionCountPerEpisode = 3)
    }

    override suspend fun resetReadingProgress(request: ResetEditionReadingProgressRequest) {
        println("RESET_READING_PROGRESS")
        increment()
        delay(Random.nextLong(10, 30))
        decrement()
        verifyRateLimiting()
    }

    private fun increment() {
        println("In-flight request count: ${counter.incrementAndGet()}")
    }

    private fun decrement() {
        counter.decrementAndGet()
    }

    @Synchronized
    private fun verifyRateLimiting() {
        val currentTime = System.currentTimeMillis()

        val iterator = rateLimitingBuffer.iterator()
        while (iterator.hasNext() && currentTime - iterator.next() > 1_000) {
            iterator.remove()
        }

        if (rateLimitingBuffer.size > MAX_REQUESTS_PER_SECOND) {
            throw IllegalStateException("Too many requests")
        }

        rateLimitingBuffer.addLast(currentTime)
    }

    private fun createMockContent(episodeCount: Int, editionCountPerEpisode: Int) = SatoriReaderSeriesContent(
        title = "",
        link = "",
        episodes = (1..episodeCount).map {
            SatoriReaderEpisode(
                title = "",
                editions = (1..editionCountPerEpisode).map {
                    SatoriReaderEdition(
                        name = "",
                        urlPath = "",
                        link = "",
                        status = SatoriReaderStatus.COMPLETED
                    )
                }
            )
        }
    )

    companion object {
        private const val MAX_REQUESTS_PER_SECOND = 100
    }
}
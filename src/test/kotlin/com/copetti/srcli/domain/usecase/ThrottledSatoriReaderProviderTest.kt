package com.copetti.srcli.domain.usecase

import com.copetti.srcli.domain.gateway.SatoriReaderProvider
import com.copetti.srcli.domain.model.*
import com.copetti.srcli.provider.satori.ThrottledSatoriReaderProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis
import kotlin.test.assertTrue

class ThrottledSatoriReaderProviderTest {

    private lateinit var satoriReaderProvider: SatoriReaderProvider
    private lateinit var throttledSatoriReaderProvider: ThrottledSatoriReaderProvider

    @BeforeEach
    fun setUp() {
        satoriReaderProvider = mockk(SatoriReaderProvider::class.simpleName)
        throttledSatoriReaderProvider = ThrottledSatoriReaderProvider(
            satoriReaderProvider = satoriReaderProvider,
            minimumDelayMs = 20
        )
    }

    @Test
    fun `should throttle all remote calls to the provider`() = runTest {
        val token = SatoriReaderLoginToken("")
        coEvery { satoriReaderProvider.login(any()) } returns token
        coEvery { satoriReaderProvider.fetchSeries() } returns listOf()
        coEvery { satoriReaderProvider.fetchSeriesContent(any()) } returns mockk()
        coEvery { satoriReaderProvider.resetReadingProgress(any()) } returns Unit

        val result = measureTimeMillis {
            throttledSatoriReaderProvider.login(LoginApplicationCredentials("", ""))
            throttledSatoriReaderProvider.fetchSeries()

            for (i in 1..10) {
                val series = SatoriReaderSeriesReference("")
                val fetchRequest = FetchSeriesContentRequest(token, series)
                throttledSatoriReaderProvider.fetchSeriesContent(fetchRequest)
            }
            for (i in 1..40) {
                val edition = SatoriReaderEdition("", "", "", SatoriReaderStatus.COMPLETED)
                throttledSatoriReaderProvider.resetReadingProgress(ResetEditionReadingProgressRequest(token, edition))
            }
        }
        assertTrue(message = "Should be at least 1 second with a min delay of 20ms") { result > 1_000 }
    }
}
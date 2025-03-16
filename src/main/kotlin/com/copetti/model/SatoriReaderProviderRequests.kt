package com.copetti.model

data class FetchAllSeriesRequest(
    val credentials: SatoriReaderCredentials
)

data class FetchSeriesContentRequest(
    val token: SatoriReaderLoginToken,
    val series: SatoriReaderSeries
)

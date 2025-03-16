package com.copetti.model

data class FetchSeriesContentRequest(
    val token: SatoriReaderLoginToken,
    val series: SatoriReaderSeriesReference
)

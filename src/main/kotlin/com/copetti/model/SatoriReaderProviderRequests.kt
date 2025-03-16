package com.copetti.model

data class FetchSeriesContentRequest(
    val token: SatoriReaderLoginToken,
    val series: SatoriReaderSeriesReference
)

data class ResetEditionReadingProgressRequest(
    val token: SatoriReaderLoginToken,
    val edition: SatoriReaderEdition
)

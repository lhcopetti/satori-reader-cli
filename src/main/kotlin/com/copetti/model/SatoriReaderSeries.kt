package com.copetti.model

data class SatoriReaderCredentials(
    val username: String,
    val password: String
)

data class SatoriReaderSeries(
    val title: String,
    val episodes: List<SatoriReaderEpisode>
)

data class SatoriReaderEpisode(
    val title: String,
    val editions: List<SatoriReaderEdition>
)

data class SatoriReaderEdition(
    val name: String,
    val url: String,
    val status: SatoriReaderStatus
)

enum class SatoriReaderStatus {
    UNREAD,
    STARTED,
    COMPLETED;
}
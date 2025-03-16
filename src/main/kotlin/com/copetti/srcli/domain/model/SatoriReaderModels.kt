package com.copetti.srcli.domain.model

data class SatoriReaderCredentials(
    val username: String,
    val password: String
)

data class SatoriReaderLoginToken(
    val sessionToken: String
)

data class SatoriReaderSeriesReference(
    val link: String
)

data class SatoriReaderSeriesContent(
    val title: String,
    val link: String,
    val episodes: List<SatoriReaderEpisode>
)


data class SatoriReaderEpisode(
    val title: String,
    val editions: List<SatoriReaderEdition>
)

data class SatoriReaderSeries(
    val title: String,
    val link: String,
    val episodes: List<SatoriReaderPrimaryEditionEpisode>
)

data class SatoriReaderPrimaryEditionEpisode(
    val title: String,
    val edition: SatoriReaderEdition
)

data class SatoriReaderEdition(
    val name: String,
    val urlPath: String,
    val link: String,
    val status: SatoriReaderStatus
)

enum class SatoriReaderStatus {
    UNREAD,
    STARTED,
    COMPLETED;
}
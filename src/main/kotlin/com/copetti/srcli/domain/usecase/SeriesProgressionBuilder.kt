package com.copetti.srcli.domain.usecase

import com.copetti.srcli.domain.model.SatoriReaderStatus

class SeriesProgressionBuilder {

    fun build(series: SeriesProgression): String {

        val sb = StringBuilder()

        val escapedSeriesTitle = escapeBacklashesAndQuotes(series.title)

        val seriesCell = buildSeriesCell(link = series.link, tooltip = "Series | $escapedSeriesTitle")
        sb.append(seriesCell)

        series.episodes.forEach { episode ->
            val escapedEpisodeTitle = escapeBacklashesAndQuotes(episode.title)
            val episodeCell = buildStatusCell(
                status = episode.status,
                link = episode.link,
                tooltip = "$escapedSeriesTitle | $escapedEpisodeTitle"
            )
            sb.append(episodeCell)
        }

        sb.appendLine()
        sb.appendLine()
        sb.appendLine("${buildSeriesCell()} - Series Title |")
        sb.appendLine("${buildStatusCell(SatoriReaderStatus.COMPLETED)} - Completed |")
        sb.appendLine("${buildStatusCell(SatoriReaderStatus.STARTED)} - Started |")
        sb.appendLine("${buildStatusCell(SatoriReaderStatus.UNREAD)} - Unread |")
        sb.append("*(hover over a cell for more information or click on it to go to the series/episode)*")

        return sb.toString()
    }

    private fun escapeBacklashesAndQuotes(title: String): String {
        return title.replace("\\", "\\\\").replace("\"", "\\\"")
    }

    private fun buildStatusCell(status: SatoriReaderStatus, link: String = "", tooltip: String = ""): String {
        val cellColor = when (status) {
            SatoriReaderStatus.UNREAD -> "ff0000"
            SatoriReaderStatus.STARTED -> "ffff00"
            SatoriReaderStatus.COMPLETED -> "00ff00"
        }
        return buildCell(cellColor, link, tooltip)
    }

    private fun buildSeriesCell(link: String = "", tooltip: String = "") = buildCell("000000", link, tooltip)

    private fun buildCell(color: String, link: String, tooltip: String): String {
        val imageLink = getImageFor(color)
        return """[![#$color]($imageLink "$tooltip")]($link)"""
    }

    private fun getImageFor(color: String) = "https://placehold.co/15x15/$color/.png?text=."
}
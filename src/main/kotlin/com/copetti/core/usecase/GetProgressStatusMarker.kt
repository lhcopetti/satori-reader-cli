package com.copetti.core.usecase

import com.copetti.model.SatoriReaderStatus

class GetProgressStatusMarker {

    fun build(series: SeriesProgression): String {

        val sb = StringBuilder()

        val seriesCell = buildCell(color = "000000", link = "https://youtube.com", tooltip = series.title)
        sb.append(seriesCell)

        series.episodes.forEach { episode ->
            val color = getCellColor(status = episode.status)
            val episodeCell = buildCell(color = color, link = "https://google.com", tooltip = episode.title)
            sb.append(episodeCell)
        }

        return sb.toString()
    }

    private fun getCellColor(status: SatoriReaderStatus) = when (status) {
        SatoriReaderStatus.UNREAD -> "ff0000"
        SatoriReaderStatus.STARTED -> "ffff00"
        SatoriReaderStatus.COMPLETED -> "00ff00"
    }

    private fun buildCell(color: String, link: String, tooltip: String): String {
        val imageLink = getImageFor(color)
        return """[![#$color]($imageLink "$tooltip")]($link)"""
    }

    private fun getImageFor(color: String) = "https://placehold.co/15x15/$color/.png?text=."


}
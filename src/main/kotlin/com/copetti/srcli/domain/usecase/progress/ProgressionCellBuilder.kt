package com.copetti.srcli.domain.usecase.progress

import com.copetti.srcli.domain.model.SatoriReaderStatus

class ProgressionCellBuilder {

    fun buildStatusCell(status: SatoriReaderStatus, link: String = "", tooltip: String = ""): String {
        val cellColor = when (status) {
            SatoriReaderStatus.UNREAD -> "ff0000"
            SatoriReaderStatus.STARTED -> "ffff00"
            SatoriReaderStatus.COMPLETED -> "00ff00"
        }
        return buildCell(cellColor, link, tooltip)
    }

    fun buildSeriesCell(link: String = "", tooltip: String = "") = buildCell("000000", link, tooltip)

    private fun buildCell(color: String, link: String, tooltip: String): String {
        val escapedTooltip = escapeBacklashesAndQuotes(tooltip)
        val imageLink = getImageFor(color)
        return """[![#$color]($imageLink "$escapedTooltip")]($link)"""
    }

    private fun getImageFor(color: String) = "https://placehold.co/15x15/$color/.png?text=."

    private fun escapeBacklashesAndQuotes(title: String): String {
        return title.replace("\\", "\\\\").replace("\"", "\\\"")
    }
}
package com.copetti.core.usecase

import com.copetti.model.SatoriReaderStatus


data class BuildProgressDashboardRequest(
    val progression: List<SeriesProgression>
)


class BuildProgressDashboard {

    fun build(request: BuildProgressDashboardRequest): String {

        val sb = StringBuilder()

        var seriesProgression = request.progression
        var grouped = seriesProgression.take(5)

        while (grouped.isNotEmpty()) {

            sb.append("|")
            val headers = grouped.map(SeriesProgression::title).joinToString(separator = "|")
            sb.append(headers)
            sb.append("|")
            sb.appendLine()

            sb.append("|")
            val headerSeparator = grouped.joinToString(separator = "|") { "---" }
            sb.append(headerSeparator)
            sb.append("|")
            sb.appendLine()

            sb.append("|")
            val progress = grouped.joinToString(separator = "|") { progress ->
                "O".repeat(progress.episodes.count { e -> e.status == SatoriReaderStatus.COMPLETED }) +
                        "X".repeat(progress.episodes.count { e -> e.status != SatoriReaderStatus.COMPLETED })
            }
            sb.append(progress)
            sb.append("|")
            sb.appendLine()

            seriesProgression = seriesProgression.drop(5)
            grouped = seriesProgression.take(5)

            if (grouped.isNotEmpty()) {
                sb.appendLine()
            }
        }

        return sb.toString()
    }

}
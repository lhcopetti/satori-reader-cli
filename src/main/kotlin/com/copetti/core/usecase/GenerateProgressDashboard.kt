package com.copetti.core.usecase


data class GenerateProgressDashboardRequest(
    val seriesProgression: List<SeriesProgression>
)

data class SeriesProgression(
    val title: String,
    val completedCount: Int,
    val unreadCount: Int
)

class GenerateProgressDashboard {

    fun generate(request: GenerateProgressDashboardRequest): String {

        val sb = StringBuilder()

        var seriesProgression = request.seriesProgression
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
                "O".repeat(progress.completedCount) +
                "X".repeat(progress.unreadCount)
            }
            sb.append(progress)
            sb.append("|")
            sb.appendLine()

            seriesProgression = seriesProgression.drop(5)
            grouped = seriesProgression.take(5)
        }

        return sb.toString()
    }
}
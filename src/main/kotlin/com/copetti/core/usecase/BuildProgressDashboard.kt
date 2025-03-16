package com.copetti.core.usecase

import com.copetti.model.SatoriReaderStatus
import java.text.NumberFormat
import java.util.*


data class BuildProgressDashboardRequest(
    val progression: List<SeriesProgression>
)


class BuildProgressDashboard(
    private val getProgressStatusMarker: GetProgressStatusMarker
) {


    fun build(request: BuildProgressDashboardRequest): String {

        val sb = StringBuilder()

        sb.appendLine()
        sb.appendLine("## Progression Dashboard")
        sb.appendLine()

        val dashboard = request.progression.sortedBy { series -> series.title }
            .joinToString(separator = " ", transform = getProgressStatusMarker::build)
        sb.appendLine(dashboard)

        sb.appendLine("### Series progression: ${getSeriesProgression(request)}")
        sb.appendLine("### Episodes progression: ${getEpisodesProgression(request)}")

        return sb.toString()
    }

    private fun getSeriesProgression(request: BuildProgressDashboardRequest): String {
        val total = request.progression.size
        val completedSeries = request.progression.count { series -> series.episodes.all(this::isEpisodeComplete) }
        val completionPercent = NUMBER_FORMATTER.format(100.0 * completedSeries / total)

        return "(${completedSeries}/${total}) - $completionPercent%"
    }

    private fun getEpisodesProgression(request: BuildProgressDashboardRequest): String {
        val total = request.progression.flatMap(SeriesProgression::episodes).size
        val completedEpisodes = request.progression.flatMap(SeriesProgression::episodes).count(this::isEpisodeComplete)
        val completionPercent = NUMBER_FORMATTER.format(100.0 * completedEpisodes / total)
        return "(${completedEpisodes}/${total}) - ${completionPercent}%"
    }

    private fun isEpisodeComplete(episodeProgression: EpisodeProgression) =
        episodeProgression.status == SatoriReaderStatus.COMPLETED

    companion object {
        private val NUMBER_FORMATTER: NumberFormat = NumberFormat.getNumberInstance(Locale.US)

        init {
            NUMBER_FORMATTER.maximumFractionDigits = 2
            NUMBER_FORMATTER.minimumFractionDigits = 2
        }
    }
}
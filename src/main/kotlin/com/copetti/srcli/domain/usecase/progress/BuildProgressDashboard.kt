package com.copetti.srcli.domain.usecase.progress

import com.copetti.srcli.domain.model.SatoriReaderStatus
import java.text.NumberFormat
import java.util.*


data class BuildProgressDashboardRequest(
    val progression: List<SeriesProgression>
)


class BuildProgressDashboard(
    private val seriesProgressionBuilder: SeriesProgressionBuilder,
    private val progressionCellBuilder: ProgressionCellBuilder
) {


    fun build(request: BuildProgressDashboardRequest): String {

        val sb = StringBuilder()

        sb.appendLine()
        sb.appendLine("## Progression Dashboard - updates daily")
        sb.appendLine()

        val dashboard = request.progression.sortedBy { series -> series.title }
            .joinToString(separator = " ", transform = seriesProgressionBuilder::build)
        sb.appendLine(dashboard)

        sb.appendLine()
        sb.appendLine("${progressionCellBuilder.buildSeriesCell()} - Series Title |")
        sb.appendLine("${progressionCellBuilder.buildStatusCell(SatoriReaderStatus.COMPLETED)} - Completed |")
        sb.appendLine("${progressionCellBuilder.buildStatusCell(SatoriReaderStatus.STARTED)} - Started |")
        sb.appendLine("${progressionCellBuilder.buildStatusCell(SatoriReaderStatus.UNREAD)} - Unread |")
        sb.appendLine("*(hover over a cell for more information or click on it to go to the series/episode)*")

        sb.appendLine()
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
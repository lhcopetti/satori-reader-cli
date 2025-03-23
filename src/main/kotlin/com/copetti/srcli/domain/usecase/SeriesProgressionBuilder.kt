package com.copetti.srcli.domain.usecase

class SeriesProgressionBuilder(
    private val progressionCellBuilder: ProgressionCellBuilder
) {

    fun build(series: SeriesProgression): String {

        val sb = StringBuilder()

        val seriesCell =
            progressionCellBuilder.buildSeriesCell(link = series.link, tooltip = "Series | ${series.title}")
        sb.append(seriesCell)

        series.episodes.forEach { episode ->
            val episodeCell = progressionCellBuilder.buildStatusCell(
                status = episode.status,
                link = episode.link,
                tooltip = "${series.title} | ${episode.title}"
            )
            sb.append(episodeCell)
        }

        return sb.toString()
    }
}
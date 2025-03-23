package com.copetti;

import com.copetti.srcli.cli.PrintAllEpisodesCommand
import com.copetti.srcli.cli.ResetReadingProgressCommand
import com.copetti.srcli.cli.SatoriReaderCliCommand
import com.copetti.srcli.cli.UpdateReadmeProgressCommand
import com.copetti.srcli.domain.usecase.*
import com.copetti.srcli.domain.usecase.cli.PrintAllEpisodeStatus
import com.copetti.srcli.domain.usecase.cli.ResetReadingProgress
import com.copetti.srcli.domain.usecase.cli.UpdateReadmeProgress
import com.copetti.srcli.provider.satori.ThrottledSatoriReaderProvider
import com.copetti.srcli.provider.satori.webcrawler.WebCrawlerSatoriReaderProvider
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) {
    val satoriReaderProvider = ThrottledSatoriReaderProvider(WebCrawlerSatoriReaderProvider())
    val fetchAllSatoriReaderContent = FetchAllSatoriReaderContent(satoriReaderProvider)
    val selectPrimaryEdition = SelectPrimaryEdition()
    val retrieveAllSatoriReaderSeries = RetrieveAllSatoriReaderSeries(fetchAllSatoriReaderContent, selectPrimaryEdition)
    val resetReadingProgress = ResetReadingProgress(satoriReaderProvider)
    val printAllEpisodeStatus = PrintAllEpisodeStatus(satoriReaderProvider, retrieveAllSatoriReaderSeries)
    val retrieveReadingProgress = RetrieveReadingProgress(satoriReaderProvider, retrieveAllSatoriReaderSeries)
    val progressionCellBuilder = ProgressionCellBuilder()
    val generateProgressDashboard = GenerateProgressDashboard(
        retrieveReadingProgress = retrieveReadingProgress,
        buildProgressDashboard = BuildProgressDashboard(
            SeriesProgressionBuilder(progressionCellBuilder),
            progressionCellBuilder
        )
    )
    val updateReadmeProgress = UpdateReadmeProgress(generateProgressDashboard = generateProgressDashboard)


    SatoriReaderCliCommand()
        .subcommands(
            ResetReadingProgressCommand(resetReadingProgress),
            PrintAllEpisodesCommand(printAllEpisodeStatus),
            UpdateReadmeProgressCommand(updateReadmeProgress)
        )
        .main(args)
}
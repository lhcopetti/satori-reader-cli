package com.copetti;

import com.copetti.srcli.cli.PrintAllEpisodesCommand
import com.copetti.srcli.cli.ResetReadingProgressCommand
import com.copetti.srcli.cli.SatoriReaderCliCommand
import com.copetti.srcli.cli.UpdateReadmeProgressCommand
import com.copetti.srcli.provider.satori.webcrawler.WebCrawlerSatoriReaderProvider
import com.copetti.srcli.domain.usecase.*
import com.copetti.srcli.domain.usecase.cli.PrintAllEpisodeStatus
import com.copetti.srcli.domain.usecase.cli.ResetReadingProgress
import com.copetti.srcli.domain.usecase.cli.UpdateReadmeProgress
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) {
    val webcrawlerSatoriReaderProvider = WebCrawlerSatoriReaderProvider()
    val fetchAllSatoriReaderContent = FetchAllSatoriReaderContent(webcrawlerSatoriReaderProvider)
    val selectPrimaryEdition = SelectPrimaryEdition()
    val retrieveAllSatoriReaderSeries = RetrieveAllSatoriReaderSeries(fetchAllSatoriReaderContent, selectPrimaryEdition)
    val resetReadingProgress = ResetReadingProgress(webcrawlerSatoriReaderProvider, fetchAllSatoriReaderContent)
    val printAllEpisodeStatus = PrintAllEpisodeStatus(webcrawlerSatoriReaderProvider, retrieveAllSatoriReaderSeries)
    val retrieveReadingProgress = RetrieveReadingProgress(webcrawlerSatoriReaderProvider, retrieveAllSatoriReaderSeries)
    val generateProgressDashboard = GenerateProgressDashboard(
        retrieveReadingProgress = retrieveReadingProgress,
        buildProgressDashboard = BuildProgressDashboard(SeriesProgressionBuilder())
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
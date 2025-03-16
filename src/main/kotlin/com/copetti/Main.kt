package com.copetti;

import com.copetti.cli.PrintAllEpisodesCommand
import com.copetti.cli.ResetReadingProgressCommand
import com.copetti.cli.SatoriReaderCliCommand
import com.copetti.cli.UpdateReadmeProgressCommand
import com.copetti.core.usecase.*
import com.copetti.provider.satori.webcrawler.WebCrawlerSatoriReaderProvider
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) {
    val webcrawlerSatoriReaderProvider = WebCrawlerSatoriReaderProvider()
    val resetReadingProgress = ResetReadingProgress(webcrawlerSatoriReaderProvider)
    val fetchAllSatoriReaderContent = FetchAllSatoriReaderContent(webcrawlerSatoriReaderProvider)
    val selectPrimaryEdition = SelectPrimaryEdition()
    val retrieveAllSatoriReaderSeries = RetrieveAllSatoriReaderSeries(fetchAllSatoriReaderContent, selectPrimaryEdition)
    val listAllEpisodes = ListAllEpisodes(retrieveAllSatoriReaderSeries)
    val retrieveReadingProgress = RetrieveReadingProgress(retrieveAllSatoriReaderSeries)
    val generateProgressDashboard = GenerateProgressDashboard(
        retrieveReadingProgress = retrieveReadingProgress,
        buildProgressDashboard = BuildProgressDashboard(GetProgressStatusMarker())
    )
    val updateReadmeProgress = UpdateReadmeProgress(generateProgressDashboard = generateProgressDashboard)

    SatoriReaderCliCommand()
        .subcommands(
            ResetReadingProgressCommand(resetReadingProgress),
            PrintAllEpisodesCommand(listAllEpisodes),
            UpdateReadmeProgressCommand(updateReadmeProgress)
        )
        .main(args)
}
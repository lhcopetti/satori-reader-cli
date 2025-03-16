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
    val fetchAllSatoriReaderContent = FetchAllSatoriReaderContent(webcrawlerSatoriReaderProvider)
    val selectPrimaryEdition = SelectPrimaryEdition()
    val retrieveAllSatoriReaderSeries = RetrieveAllSatoriReaderSeries(fetchAllSatoriReaderContent, selectPrimaryEdition)
    val resetReadingProgress = ResetReadingProgress(webcrawlerSatoriReaderProvider, fetchAllSatoriReaderContent)
    val listAllEpisodes = ListAllEpisodes(webcrawlerSatoriReaderProvider, retrieveAllSatoriReaderSeries)
    val retrieveReadingProgress = RetrieveReadingProgress(webcrawlerSatoriReaderProvider, retrieveAllSatoriReaderSeries)
    val generateProgressDashboard = GenerateProgressDashboard(
        retrieveReadingProgress = retrieveReadingProgress,
        buildProgressDashboard = BuildProgressDashboard(SeriesProgressionBuilder())
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
package com.copetti;

import com.copetti.srcli.cli.PrintAllEpisodesCommand
import com.copetti.srcli.cli.ResetReadingProgressCommand
import com.copetti.srcli.cli.SatoriReaderCliCommand
import com.copetti.srcli.cli.UpdateReadmeProgressCommand
import com.copetti.srcli.domain.usecase.FetchAllSatoriReaderContent
import com.copetti.srcli.domain.usecase.RetrieveAllSatoriReaderSeries
import com.copetti.srcli.domain.usecase.SelectPrimaryEdition
import com.copetti.srcli.domain.usecase.cli.AuthenticateUser
import com.copetti.srcli.domain.usecase.cli.PrintAllEpisodeStatus
import com.copetti.srcli.domain.usecase.cli.ResetReadingProgress
import com.copetti.srcli.domain.usecase.cli.UpdateReadmeProgress
import com.copetti.srcli.domain.usecase.file.FileSystem
import com.copetti.srcli.domain.usecase.progress.*
import com.copetti.srcli.provider.satori.ThrottledSatoriReaderProvider
import com.copetti.srcli.provider.satori.webcrawler.WebCrawlerSatoriReaderProvider
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) {
    val satoriReaderProvider = ThrottledSatoriReaderProvider(WebCrawlerSatoriReaderProvider())
    val fetchAllSatoriReaderContent = FetchAllSatoriReaderContent(satoriReaderProvider)
    val selectPrimaryEdition = SelectPrimaryEdition()
    val retrieveAllSatoriReaderSeries = RetrieveAllSatoriReaderSeries(fetchAllSatoriReaderContent, selectPrimaryEdition)
    val authenticateUser = AuthenticateUser(satoriReaderProvider)
    val resetReadingProgress = ResetReadingProgress(authenticateUser, satoriReaderProvider)
    val printAllEpisodeStatus = PrintAllEpisodeStatus(authenticateUser, retrieveAllSatoriReaderSeries)
    val retrieveReadingProgress = RetrieveReadingProgress(authenticateUser, retrieveAllSatoriReaderSeries)
    val progressionCellBuilder = ProgressionCellBuilder()
    val generateProgressDashboard = GenerateProgressDashboard(
        retrieveReadingProgress = retrieveReadingProgress,
        buildProgressDashboard = BuildProgressDashboard(
            SeriesProgressionBuilder(progressionCellBuilder),
            progressionCellBuilder
        )
    )
    val updateReadmeProgress = UpdateReadmeProgress(generateProgressDashboard, FileSystem())


    SatoriReaderCliCommand()
        .subcommands(
            ResetReadingProgressCommand(resetReadingProgress),
            PrintAllEpisodesCommand(printAllEpisodeStatus),
            UpdateReadmeProgressCommand(updateReadmeProgress)
        )
        .main(args)
}
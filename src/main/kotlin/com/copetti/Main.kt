package com.copetti;

import com.copetti.cli.GenerateProgressDashboardCommand
import com.copetti.cli.PrintAllEpisodesCommand
import com.copetti.cli.ResetReadingProgressCommand
import com.copetti.cli.SatoriReaderCliCommand
import com.copetti.core.usecase.*
import com.copetti.provider.satori.SatoriReaderProviderLocator
import com.copetti.provider.satori.selenium.SeleniumSatoriReaderProvider
import com.copetti.provider.satori.selenium.SeleniumWebDriverConfiguration
import com.copetti.provider.satori.webcrawler.WebCrawlerSatoriReaderProvider
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) {

    val driver = SeleniumWebDriverConfiguration()
    try {
        val webcrawlerSatoriReaderProvider = WebCrawlerSatoriReaderProvider()
        val seleniumSatoriReaderProvider = SeleniumSatoriReaderProvider(SeleniumWebDriverConfiguration())
        val satoriReaderProviderLocator = SatoriReaderProviderLocator(
            webcrawlerSatoriReaderProvider,
            seleniumSatoriReaderProvider
        )
        val resetReadingProgress = ResetReadingProgress(satoriReaderProviderLocator)
        val listAllEpisodes = ListAllEpisodes(satoriReaderProviderLocator)
        val retrieveReadingProgress = RetrieveReadingProgress(
            selectPrimaryEdition = SelectPrimaryEdition(),
            satoriReaderProviderLocator = satoriReaderProviderLocator
        )
        val generateProgressDashboard = GenerateProgressDashboard(
            retrieveReadingProgress = retrieveReadingProgress,
            buildProgressDashboard = BuildProgressDashboard()
        )

        SatoriReaderCliCommand()
            .subcommands(
                ResetReadingProgressCommand(resetReadingProgress),
                PrintAllEpisodesCommand(listAllEpisodes),
                GenerateProgressDashboardCommand(generateProgressDashboard)
            )
            .main(args)

    } finally {
        driver.quit()
    }
}
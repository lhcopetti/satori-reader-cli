package com.copetti;

import com.copetti.cli.PrintAllEpisodesCommand
import com.copetti.cli.ResetReadingProgressCommand
import com.copetti.cli.SatoriReaderCliCommand
import com.copetti.core.usecase.ListAllEpisodes
import com.copetti.core.usecase.ResetReadingProgress
import com.copetti.provider.selenium.SeleniumSatoriReaderProvider
import com.copetti.provider.selenium.SeleniumWebDriverConfiguration
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands

private const val CONFIG_PATH = "./config.properties"

fun main(args: Array<String>) {

    val driver = SeleniumWebDriverConfiguration()
    try {
        val repository = SeleniumSatoriReaderProvider(driver)
        val resetReadingProgress = ResetReadingProgress(repository)
        val listAllEpisodes = ListAllEpisodes(repository)

        SatoriReaderCliCommand()
            .subcommands(
                ResetReadingProgressCommand(resetReadingProgress),
                PrintAllEpisodesCommand(listAllEpisodes)
            )
            .main(args)

    } finally {
        driver.quit()
    }
}

package com.copetti;

import com.copetti.cli.PrintAllEpisodesCommand
import com.copetti.cli.ResetReadingProgressCommand
import com.copetti.core.usecase.ListAllEpisodes
import com.copetti.core.usecase.ResetReadingProgress
import com.copetti.provider.selenium.SeleniumSatoriReaderRepository
import com.copetti.provider.selenium.SeleniumWebDriverConfiguration
import com.github.ajalt.clikt.core.CoreNoOpCliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands

private const val CONFIG_PATH = "./config.properties"

fun main(args: Array<String>) {
//    ResetSatoriReaderApp(CONFIG_PATH).run()

//    val properties = AppProperties.load(configPath)
//    val options = ChromeOptions()
//    options.setPageLoadStrategy(PageLoadStrategy.EAGER)
//    val driver = ChromeDriver(options)
//    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10))

    val driver = SeleniumWebDriverConfiguration()
    try {
        val repository = SeleniumSatoriReaderRepository(driver)
        val resetReadingProgress = ResetReadingProgress(repository)
        val listAllEpisodes = ListAllEpisodes(repository)

        CoreNoOpCliktCommand()
            .subcommands(
                ResetReadingProgressCommand(resetReadingProgress),
                PrintAllEpisodesCommand(listAllEpisodes)
            )
            .main(args)

    } finally {
        driver.quit()
    }
}

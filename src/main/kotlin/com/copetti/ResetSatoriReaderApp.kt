package com.copetti

import com.copetti.core.SatoriReaderRepositoryRequest
import com.copetti.model.SatoriReaderEdition
import com.copetti.model.SatoriReaderSeries
import com.copetti.provider.selenium.SeleniumSatoriReaderRepository
import com.copetti.provider.selenium.SeleniumWebDriverConfiguration
import java.util.*
import java.util.stream.Collectors


class ResetSatoriReaderApp(
    val configPath: String
) {

    fun run() {

        val properties = AppProperties.load(configPath)

        val driver = SeleniumWebDriverConfiguration()
        try {
            val repository = SeleniumSatoriReaderRepository(driver)
            val request = SatoriReaderRepositoryRequest(
                login = properties.getUsername()!!,
                password = properties.getPassword()!!
            )
            repository.login(request)

            val allSeries = repository.fetchAllSeries()

//            printAllEditions(allSeries)
            printEpisodes(allSeries)


        } finally {
            driver.quit()
        }

    }

    private fun printAllEditions(allSeries: List<SatoriReaderSeries>) {
        val allEditions = allSeries.stream()
            .flatMap { series -> series.episodes.stream() }
            .flatMap { episode -> episode.editions.stream() }
            .map(SatoriReaderEdition::name)
            .collect(Collectors.toSet())
        allEditions.forEach(System.out::println)
    }

    private fun printEpisodes(allSeries: List<SatoriReaderSeries>) {
        val skippedEpisodes = mutableListOf<String>()
        val rankedEditions = listOf(
            "HARDER (SFX)",
            "HARDER",
            "HARDER (VOICE ONLY)",
            "WITH SOUND EFFECTS",
            "STANDARD",
            "NO SPACES",
            "NO KATAKANA",
            "MEDIUM",
            "EASIER",
            "EASIER (SFX)",
            "EASIER (VOICE ONLY)",
            "VOICE ONLY",
            "WITH SPACES (20 MINUTES)",
            "WITH SPACES (10 MINUTES)",
        )
        for (series in allSeries) {

            for (episode in series.episodes) {

                val editionsByName = episode.editions.stream().collect(Collectors.toMap({ k -> k.name }, { v -> v }))
                val selectedEdition = rankedEditions.stream()
                    .map { rankedEdition -> editionsByName[rankedEdition] }
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow { IllegalStateException("Could not find a ranked edition") }!!

                println("${series.title},${selectedEdition.name},${selectedEdition.status},${selectedEdition.url}")
            }

        }

        skippedEpisodes.forEach(System.out::println)
    }

}
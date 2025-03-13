package com.copetti.cli

import com.copetti.core.usecase.ListAllEpisodes
import com.copetti.core.usecase.ListAllEpisodesRequest
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject

class PrintAllEpisodesCommand(
    private val listAllEpisodes: ListAllEpisodes,
) : CliktCommand() {

    private val config by requireObject<SatoriReaderCliContext>()

    override fun run() {
        val request = ListAllEpisodesRequest(credentials = config.credentials)
        val allEpisodes = listAllEpisodes.print(request)
        allEpisodes.forEach { episode ->
            echo("${episode.title},${episode.edition},${episode.status},${episode.link}")
        }
    }

}
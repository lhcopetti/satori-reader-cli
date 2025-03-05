package com.copetti.cli

import com.copetti.core.model.SatoriCredentials
import com.copetti.core.usecase.ListAllEpisodesRequest
import com.copetti.core.usecase.ListAllEpisodes
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.provideDelegate

class PrintAllEpisodesCommand(
    private val listAllEpisodes: ListAllEpisodes,
) : CliktCommand() {

    val baseOptions by BaseOptions()

    override fun run() {
        val username = baseOptions.username
        val password = baseOptions.password

        val request = ListAllEpisodesRequest(
            credentials = SatoriCredentials(
                login = username,
                password = password
            )
        )
        val allEpisodes = listAllEpisodes.print(request)
        allEpisodes.forEach { episode ->
            echo("${episode.title},${episode.edition},${episode.status},${episode.link}")
        }
    }


}
package com.copetti.srcli.cli

import com.copetti.srcli.domain.usecase.cli.ListAllEpisodesRequest
import com.copetti.srcli.domain.usecase.cli.PrintAllEpisodeStatus
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject

class PrintAllEpisodesCommand(
    private val printAllEpisodeStatus: PrintAllEpisodeStatus,
) : CliktCommand() {

    private val config by requireObject<SatoriReaderCliContext>()

    override fun run() {
        val request = ListAllEpisodesRequest(credentials = config.credentials)
        val result = printAllEpisodeStatus.print(request)
        echo(result)
    }

}
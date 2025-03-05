package com.copetti.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.options.required

class BaseOptions : OptionGroup() {
    val username: String by option().required()
    val password: String by option().prompt(hideInput = true)
}
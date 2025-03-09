package com.copetti.core.usecase

import com.copetti.model.SatoriReaderEdition
import com.copetti.model.SatoriReaderEpisode
import java.util.*
import java.util.stream.Collectors

class SelectPrimaryEdition {

    fun select(episode: SatoriReaderEpisode): SatoriReaderEdition {
        val editionsByName = episode.editions.stream().collect(Collectors.toMap({ k -> k.name }, { v -> v }))
        val selectedEdition = RANKED_EDITIONS.stream()
            .map { rankedEdition -> editionsByName[rankedEdition] }
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow { IllegalStateException("Could not find a primary edition") }!!

        return selectedEdition
    }

    companion object {
        private val RANKED_EDITIONS = listOf(
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
    }
}
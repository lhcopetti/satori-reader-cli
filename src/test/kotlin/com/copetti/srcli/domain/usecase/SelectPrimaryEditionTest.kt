package com.copetti.srcli.domain.usecase

import com.copetti.srcli.domain.model.SatoriReaderEdition
import com.copetti.srcli.domain.model.SatoriReaderEpisode
import com.copetti.srcli.domain.model.SatoriReaderStatus
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SelectPrimaryEditionTest {

    @InjectMockKs
    private lateinit var selectPrimaryEdition: SelectPrimaryEdition

    @Test
    fun `should select the harder edition`() {

        val easier = SatoriReaderEdition("EASIER", "the-url", "link", SatoriReaderStatus.COMPLETED)
        val harder = SatoriReaderEdition("HARDER", "the-url", "link", SatoriReaderStatus.COMPLETED)
        val episode = SatoriReaderEpisode(
            title = "the-title",
            editions = listOf(easier, harder)
        )

        val result = selectPrimaryEdition.select(episode)
        assertEquals(harder, result)
    }

    @Test
    fun `should select the harder edition that contains sfx`() {

        val easier = SatoriReaderEdition("EASIER", "the-url", "link", SatoriReaderStatus.COMPLETED)
        val harder = SatoriReaderEdition("HARDER", "the-url", "link", SatoriReaderStatus.COMPLETED)
        val easierSfx = SatoriReaderEdition("EASIER (SFX)", "the-url", "link", SatoriReaderStatus.COMPLETED)
        val harderSfx = SatoriReaderEdition("HARDER (SFX)", "the-url", "link", SatoriReaderStatus.COMPLETED)
        val episode = SatoriReaderEpisode(
            title = "the-title",
            editions = listOf(easier, harder, easierSfx, harderSfx)
        )

        val result = selectPrimaryEdition.select(episode)
        assertEquals(harderSfx, result)
    }

    @Test
    fun `should select the medium edition`() {

        val easier = SatoriReaderEdition("EASIER", "the-url", "link", SatoriReaderStatus.COMPLETED)
        val medium = SatoriReaderEdition("MEDIUM", "the-url", "link", SatoriReaderStatus.COMPLETED)
        val episode = SatoriReaderEpisode(
            title = "the-title",
            editions = listOf(easier, medium)
        )

        val result = selectPrimaryEdition.select(episode)
        assertEquals(medium, result)
    }

    @Test
    fun `should select the edition with sound effects`() {

        val withSoundEffects =
            SatoriReaderEdition("With Sound Effects", "the-url", "link", SatoriReaderStatus.COMPLETED)
        val voiceOnly = SatoriReaderEdition("Voice Only", "the-url", "link", SatoriReaderStatus.COMPLETED)
        val episode = SatoriReaderEpisode(
            title = "the-title",
            editions = listOf(withSoundEffects, voiceOnly)
        )

        val result = selectPrimaryEdition.select(episode)
        assertEquals(withSoundEffects, result)
    }
}
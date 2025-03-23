package com.copetti.srcli.domain.usecase.progress

import com.copetti.srcli.domain.model.SatoriReaderStatus
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class ProgressionCellBuilderTest {

    @InjectMockKs
    private lateinit var progressionCellBuilder: ProgressionCellBuilder

    @Test
    fun `should build the series cell correctly`() {
        val actual = progressionCellBuilder.buildSeriesCell("link", "tooltip")
        val expected = """[![#000000](https://placehold.co/15x15/000000/.png?text=. "tooltip")](link)"""
        assertEquals(expected, actual)
    }

    @Test
    fun `should build the cell correctly for completed status`() {
        val actual = progressionCellBuilder.buildStatusCell(SatoriReaderStatus.COMPLETED, "link", "tooltip")
        val expected = """[![#00ff00](https://placehold.co/15x15/00ff00/.png?text=. "tooltip")](link)"""
        assertEquals(expected, actual)
    }

    @Test
    fun `should build the cell correctly for unread status`() {
        val actual = progressionCellBuilder.buildStatusCell(SatoriReaderStatus.UNREAD, "link", "tooltip")
        val expected = """[![#ff0000](https://placehold.co/15x15/ff0000/.png?text=. "tooltip")](link)"""
        assertEquals(expected, actual)
    }

    @Test
    fun `should build the cell correctly for started status`() {
        val actual = progressionCellBuilder.buildStatusCell(SatoriReaderStatus.STARTED, "link", "tooltip")
        val expected = """[![#ffff00](https://placehold.co/15x15/ffff00/.png?text=. "tooltip")](link)"""
        assertEquals(expected, actual)
    }

    @Test
    fun `should escape quotes and backlashes from the tooltip for series cell`() {
        val tooltip = "series title with \"quotes\" and \\backlashes\\ ! Be careful"
        val actual = progressionCellBuilder.buildSeriesCell("link", tooltip)
        val expected =
            """[![#000000](https://placehold.co/15x15/000000/.png?text=. "series title with \"quotes\" and \\backlashes\\ ! Be careful")](link)"""
        assertEquals(expected, actual)
    }

    @Test
    fun `should escape quotes and backlashes from the tooltip for episode cell`() {
        val tooltip = "series title with \"quotes\" and \\backlashes\\ ! Be careful"
        val actual = progressionCellBuilder.buildStatusCell(SatoriReaderStatus.COMPLETED, "link", tooltip)
        val expected =
            """[![#00ff00](https://placehold.co/15x15/00ff00/.png?text=. "series title with \"quotes\" and \\backlashes\\ ! Be careful")](link)"""
        assertEquals(expected, actual)
    }

}
package com.copetti.srcli.domain.usecase.cli

import com.copetti.srcli.domain.model.SatoriReaderCredentials
import com.copetti.srcli.domain.usecase.file.FileSystem
import com.copetti.srcli.domain.usecase.progress.GenerateProgressDashboard
import com.copetti.srcli.domain.usecase.progress.GenerateProgressDashboardRequest
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.Path

@ExtendWith(MockKExtension::class)
class UpdateReadmeProgressTest {

    @MockK
    private lateinit var generateProgressDashboard: GenerateProgressDashboard

    @MockK
    private lateinit var fileSystem: FileSystem

    @InjectMockKs
    private lateinit var updateReadmeProgress: UpdateReadmeProgress

    @Test
    fun `should generate dashboard and save the new readme files`() {
        val credentials = SatoriReaderCredentials(username = "the-username", password = "the-password")
        val templateContent = "- {{PROGRESSION_DASHBOARD}} - {{TODAY}} - template-content"

        every { fileSystem.readFile(any()) } returns templateContent
        every { fileSystem.writeFile(any(), any()) } returns Unit

        every { generateProgressDashboard.generate(any()) } returns "dashboard"

        updateReadmeProgress.update(UpdateReadmeRequest(credentials))

        verify { generateProgressDashboard.generate(GenerateProgressDashboardRequest(credentials)) }
        verify { fileSystem.readFile(Path("./README.md.template")) }

        val today = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val expectedContent = "- dashboard - $today - template-content"
        verify { fileSystem.writeFile(Path("./README.md"), expectedContent) }
        verify { fileSystem.writeFile(Path("./history/README-$today.md"), expectedContent) }
    }

}
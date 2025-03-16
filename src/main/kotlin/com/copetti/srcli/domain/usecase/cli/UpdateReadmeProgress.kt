package com.copetti.srcli.domain.usecase.cli

import com.copetti.srcli.domain.model.SatoriReaderCredentials
import com.copetti.srcli.domain.usecase.GenerateProgressDashboard
import com.copetti.srcli.domain.usecase.GenerateProgressDashboardRequest
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.Path

data class UpdateReadmeRequest(
    val credentials: SatoriReaderCredentials
)

class UpdateReadmeProgress(
    private val generateProgressDashboard: GenerateProgressDashboard
) {

    fun update(request: UpdateReadmeRequest) {

        val readMeTemplateContent = Files.readString(Path(README_TEMPLATE))

        val readMeContent = readMeTemplateContent
            .replace(DASHBOARD_TEMPLATE_KEY, getProgressionDashboard(request))
            .replace(TODAY_TEMPLATE_KEY, getDateToday())

        Files.writeString(Path(README), readMeContent)
        Files.writeString(Path(getHistoryReadme()), readMeContent)
    }

    private fun getHistoryReadme() = "$HISTORY_FOLDER/README-${getDateToday()}.md"

    private fun getProgressionDashboard(request: UpdateReadmeRequest): String {
        val generateRequest = GenerateProgressDashboardRequest(
            credentials = request.credentials
        )
        return generateProgressDashboard.generate(generateRequest)
    }

    private fun getDateToday() = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

    companion object {
        private const val README = "./README.md"
        private const val README_TEMPLATE = "$README.template"

        private const val HISTORY_FOLDER = "./history"

        private const val DASHBOARD_TEMPLATE_KEY = "{{PROGRESSION_DASHBOARD}}"
        private const val TODAY_TEMPLATE_KEY = "{{TODAY}}"
    }
}

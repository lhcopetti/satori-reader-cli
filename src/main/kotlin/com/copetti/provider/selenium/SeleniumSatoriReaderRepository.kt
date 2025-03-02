package com.copetti.provider.selenium

import com.copetti.core.SatoriReaderRepository
import com.copetti.core.SatoriReaderRepositoryRequest
import com.copetti.model.SatoriReaderEdition
import com.copetti.model.SatoriReaderEpisode
import com.copetti.model.SatoriReaderSeries
import com.copetti.model.SatoriReaderStatus
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.WindowType

class SeleniumSatoriReaderRepository(
    private val driver: WebDriver
) : SatoriReaderRepository {
    override fun login(request: SatoriReaderRepositoryRequest) {
        openSatoriReaderWebSite(driver)
        val signIn = driver.findElement(By.xpath("//a[@href=\"/signin\"]"))
        signIn.click()

        val usernameInput = driver.findElement(By.id("username"))
        val passwordInput = driver.findElement(By.id("password"))
        val signInButton = driver.findElement(By.id("sign-in-button"))

        usernameInput.sendKeys(request.login)
        passwordInput.sendKeys(request.password)
        signInButton.click()
    }

    override fun fetchAllSeries(request: SatoriReaderRepositoryRequest): List<SatoriReaderSeries> {

        val seriesTiles = driver.findElement(By.className("series-tiles"))
        val allSeries = seriesTiles.findElements(By.tagName("a"))
        val series = mutableListOf<SatoriReaderSeries>()

        for (satoriSeries in allSeries) {
            openLinkInNewTab(satoriSeries)

            val title = getSeriesTitle()
            val episodes = getSeriesEpisodes()
            val satoriReaderSeries = SatoriReaderSeries(
                title = title,
                episodes = episodes
            )
            series.add(satoriReaderSeries)

            closeTab()
        }

        return series
    }

    override fun resetProgress(allSeries: List<SatoriReaderSeries>) {

        val startedEditions = allSeries.stream()
            .flatMap { series -> series.episodes.stream() }
            .flatMap { episode -> episode.editions.stream() }
            .filter { edition -> edition.status != SatoriReaderStatus.UNREAD }
            .toList()

        val groupedEditions = inGroupsOf(startedEditions, 10)

        groupedEditions.forEach { editions ->
            editions.forEach { edition -> openLinkInNewTab(edition.url) }
            editions.forEach {
                driver.findElement(By.id("sidebar-article-status-unread")).click()
                Thread.sleep(250)
                closeTab()
            }
        }
    }

    private fun inGroupsOf(elements: List<SatoriReaderEdition>, groupSize: Int): List<List<SatoriReaderEdition>> {

        val result = mutableListOf<List<SatoriReaderEdition>>()
        var remainingElements = elements

        while (remainingElements.isNotEmpty()) {
            val newBatch = remainingElements.take(groupSize)
            remainingElements = remainingElements.drop(groupSize)
            result.add(newBatch)
        }

        return result
    }

    private fun getSeriesTitle(): String {
        return driver //
            .findElement(By.className("series-detail-title-description-area"))
            .findElement(By.tagName("h1"))
            .text
    }

    private fun getSeriesEpisodes(): List<SatoriReaderEpisode> {
        return driver
            .findElements(By.className("series-detail-grid-item"))
            .stream()
            .map { episode ->
                val title = episode.findElement(By.className("title")).text
                val editions = episode //
                    .findElement(By.tagName("table"))
                    .findElements(By.tagName("tr"))
                    .drop(1) // skip header
                    .map { row ->
                        val columns = row.findElements(By.tagName("td"))
                        val name = columns[0].text
                        val url = columns[0].findElement(By.tagName("a")).getDomAttribute("href")!!
                        val status = SatoriReaderStatus.entries
                            .first { st -> st.toString().equals(columns[1].text, true) }
                        SatoriReaderEdition(name = name, url = url, status = status)
                    }
                SatoriReaderEpisode(title = title, editions = editions)
            }
            .toList()
    }


    private fun openLinkInNewTab(linkWebElement: WebElement) {
        val href = linkWebElement.getDomAttribute("href")
        driver.switchTo().newWindow(WindowType.TAB)
        driver.get("${SATORI_BASE_URL}${href}")
    }

    private fun openLinkInNewTab(episodeUrl: String) {
        driver.switchTo().newWindow(WindowType.TAB)
        driver.get("${SATORI_BASE_URL}${episodeUrl}")
    }

    private fun closeTab() {
        driver.close()
        driver.switchTo().window(driver.windowHandles.last())
    }

    private fun openSatoriReaderWebSite(driver: WebDriver) {
        driver.get("https://www.satorireader.com/series")
        driver.manage().window().maximize()
    }

    companion object {
        private const val SATORI_BASE_URL = "https://satorireader.com"
    }
}
package com.copetti

import org.openqa.selenium.By
import org.openqa.selenium.PageLoadStrategy
import org.openqa.selenium.WebElement
import org.openqa.selenium.WindowType
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.time.Duration


class ResetSatoriReaderApp(
    val configPath: String
) {

    fun run() {

        val properties = AppProperties.load(configPath)
        val options = ChromeOptions()
        options.setPageLoadStrategy(PageLoadStrategy.EAGER)
        val driver = ChromeDriver(options)

        try {
            resetHistory(properties, driver)
        } finally {
            driver.quit()
        }

    }

    private fun resetHistory(properties: AppProperties, driver: ChromeDriver) {
        val baseUrl = "https://satorireader.com"
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10))
        openSatoriReaderWebSite(driver)
        login(driver, properties)

        val seriesTiles = driver.findElement(By.className("series-tiles"))
        val allSeries = seriesTiles.findElements(By.tagName("a"))
//        allSeries = allSeries.drop(2)

        for (series in allSeries) {

            openLinkInNewTab(baseUrl, driver, series)

            val allEpisodes = driver.findElements(By.className("series-detail-grid-item"))


            val pendingEpisodeLinks = allEpisodes.stream()
                .flatMap { episode ->
                    val editionsTable = episode.findElement(By.tagName("table"))
                    val allRowsWithHeader = editionsTable.findElements(By.tagName("tr"))
                    allRowsWithHeader.drop(1).stream()
                }
                .filter { row ->
                    val columns = row.findElements(By.tagName("td"))
                    val status = columns[1].text
                    status != "Unread"
                }
                .map { row ->
                    val columns = row.findElements(By.tagName("td"))
                    columns[0].findElement(By.tagName("a")).getDomAttribute("href")
                }
                .toList()

            for(episode in pendingEpisodeLinks) {
                openLinkInNewTab(baseUrl, driver, episode)
            }

            for(episode in pendingEpisodeLinks) {
                driver.findElement(By.id("sidebar-article-status-unread")).click()
                closeTab(driver)
            }

//            for (episode in allEpisodes) {
//
//                val editionsTable = episode.findElement(By.tagName("table"))
//                val allRowsWithHeader = editionsTable.findElements(By.tagName("tr"))
//                val rows = allRowsWithHeader.drop(1)
//
//
//                for (row in rows) {
//                    val columns = row.findElements(By.tagName("td"))
//
//                    val status = columns[1].text
//
//                    if (status == "Unread")
//                        continue;
//
//                    val edition = columns[0].findElement(By.tagName("a"))
//                    openLinkInNewTab(baseUrl, driver, edition)
//
//                    closeTab(driver)
//                }
//
//            }

            closeTab(driver)
        }

    }
    private fun openLinkInNewTab(baseUrl: String, driver: ChromeDriver, episodeUrl: String) {
        driver.switchTo().newWindow(WindowType.TAB)
        driver.get("${baseUrl}${episodeUrl}")
    }

    private fun openLinkInNewTab(baseUrl: String, driver: ChromeDriver, linkWebElement: WebElement) {
        val href = linkWebElement.getDomAttribute("href")
        driver.switchTo().newWindow(WindowType.TAB)
        driver.get("${baseUrl}${href}")
    }

    private fun closeTab(driver: ChromeDriver) {
        driver.close()
        driver.switchTo().window(driver.windowHandles.last())
    }

    private fun login(driver: ChromeDriver, properties: AppProperties) {
        val signIn = driver.findElement(By.xpath("//a[@href=\"/signin\"]"))
        signIn.click()

        val usernameInput = driver.findElement(By.id("username"))
        val passwordInput = driver.findElement(By.id("password"))
        val signInButton = driver.findElement(By.id("sign-in-button"))

        usernameInput.sendKeys(properties.getUsername())
        passwordInput.sendKeys(properties.getPassword())
        signInButton.click()
    }

    private fun openSatoriReaderWebSite(driver: ChromeDriver) {
        driver.get("https://www.satorireader.com/series")
        driver.manage().window().maximize()
    }
}
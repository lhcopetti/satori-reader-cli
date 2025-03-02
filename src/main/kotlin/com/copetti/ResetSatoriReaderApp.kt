package com.copetti

import com.copetti.core.SatoriReaderRepositoryRequest
import com.copetti.model.SatoriReaderStatus
import com.copetti.provider.selenium.SeleniumSatoriReaderRepository
import org.openqa.selenium.By
import org.openqa.selenium.PageLoadStrategy
import org.openqa.selenium.WebElement
import org.openqa.selenium.WindowType
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions


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

        val repository = SeleniumSatoriReaderRepository(driver)
        val request = SatoriReaderRepositoryRequest(
            login = properties.getUsername()!!,
            password = properties.getPassword()!!
        )

        val allSeries = repository.fetchAllSeries(request)

        for (series in allSeries) {

            val startedEditions = series.episodes.stream()
                .flatMap { episode -> episode.editions.stream() }
                .filter { edition -> edition.status != SatoriReaderStatus.UNREAD }
                .toList()

            startedEditions.forEach { edition ->  openLinkInNewTab(baseUrl, driver, edition.url) }
            startedEditions.forEach {
                driver.findElement(By.id("sidebar-article-status-unread")).click()
                Thread.sleep(250)
                closeTab(driver)
            }
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
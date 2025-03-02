package com.copetti

import com.copetti.core.SatoriReaderRepositoryRequest
import com.copetti.provider.selenium.SeleniumSatoriReaderRepository
import org.openqa.selenium.PageLoadStrategy
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
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10))

        try {
            val repository = SeleniumSatoriReaderRepository(driver)
            val request = SatoriReaderRepositoryRequest(
                login = properties.getUsername()!!,
                password = properties.getPassword()!!
            )
            repository.login(request)

            val allSeries = repository.fetchAllSeries(request)
            repository.resetProgress(allSeries)

        } finally {
            driver.quit()
        }

    }

}
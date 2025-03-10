package com.copetti.provider.satori.selenium

import org.openqa.selenium.PageLoadStrategy
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.time.Duration

class SeleniumWebDriverConfiguration {

    private val webDriver = lazy {
        val options = ChromeOptions()
        options.setPageLoadStrategy(PageLoadStrategy.EAGER)
        val driver = ChromeDriver(options)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10))
        driver
    }

    fun getDriver() = webDriver.value

    fun quit() {
        if (!webDriver.isInitialized()) {
            return
        }
        webDriver.value.quit()
    }
}
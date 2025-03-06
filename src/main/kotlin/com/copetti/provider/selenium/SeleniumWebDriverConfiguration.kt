package com.copetti.provider.selenium

import org.openqa.selenium.PageLoadStrategy
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.time.Duration

class SeleniumWebDriverConfiguration {

    private val webDriver: WebDriver by lazy {
        val options = ChromeOptions()
//        options.addArguments("--headless=new")
        options.setPageLoadStrategy(PageLoadStrategy.EAGER)
        val driver = ChromeDriver(options)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10))
        driver
    }

    fun getDriver() = webDriver

    fun quit() = webDriver.quit()
}
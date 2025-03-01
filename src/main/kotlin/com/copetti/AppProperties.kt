package com.copetti

import java.io.FileInputStream
import java.util.Properties

data class AppProperties(
    private val properties: Properties
) {

    fun getUsername(): String? = properties.getProperty(USERNAME_KEY)
    fun getPassword(): String? = properties.getProperty(PASSWORD_KEY)


    companion object {

        private const val USERNAME_KEY = "app.username"
        private const val PASSWORD_KEY = "app.password"

        fun load(path: String): AppProperties {
            val properties = Properties()
            FileInputStream(path).use (properties::load)
            return AppProperties(properties)
        }
    }
}
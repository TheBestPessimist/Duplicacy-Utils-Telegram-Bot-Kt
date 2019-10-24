package tbp.land

import java.io.FileInputStream
import java.util.*

object TheConfig {
    lateinit var TELEGRAM_API_TOKEN: String
    lateinit var WEBSERVER_ADDRESS: String

    init {
        readPropertiesFile()
    }

    private fun readPropertiesFile() {
        val properties: Properties = Properties().apply {
            FileInputStream("resources/configuration.properties").use { this.load(it) }
        }
        TELEGRAM_API_TOKEN = properties["telegram_api_token"] as String
        WEBSERVER_ADDRESS = properties["webserver_address"] as String
    }
}



package tbp.land.main.kotlin

import java.io.FileInputStream
import java.util.*

object TelegramConfig {

    lateinit var TELEGRAM_API_TOKEN: String
    lateinit var WEBSERVER_ADDRESS: String
    lateinit var CERTIFICATE_PATH: String

    lateinit var properties: Properties

    init {
        readPropertiesFile()
        retrieveProperties()
    }

    private fun readPropertiesFile() {
        properties = Properties().apply {
            FileInputStream("resources/configuration.properties").use { this.load(it) }
        }
    }

    private fun retrieveProperties() {
        TELEGRAM_API_TOKEN =
            readStringProperty("telegram_api_token")
        WEBSERVER_ADDRESS =
            readStringProperty("webserver_address")
        CERTIFICATE_PATH =
            readStringProperty("certificate_path")
    }

    private fun readStringProperty(propertyName: String) = (properties[propertyName] as String).trim()
}

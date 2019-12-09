package tbp.land.main.kotlin

import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths
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
            val s = getConfigurationFilePath()
            println("configuration.properties full path: ${Paths.get(s).toAbsolutePath()}")
            FileInputStream(s).use { this.load(it) }
        }
    }

    /**
     * Test if the config file exists at the root of the project (docker) or in the resources folder (ide)
     */
    private fun getConfigurationFilePath(): String {
        val path = "configuration.properties"

        return if (Files.isRegularFile(Paths.get(path))) {
            path
        } else {
            "src/main/resources/configuration.properties"
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

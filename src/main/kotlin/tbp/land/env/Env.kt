package tbp.land.env

import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * Reads and resolves properties and variables.
 * Sources:
 * - properties file
 * - .env file
 * - System env
 */
object Env {
    private val env = mutableMapOf<String, String>()

    init {
        loadProperties()
        println("Env: ")
        env.entries.forEach {
            println("    ${it.key}: ${it.value}")
        }
    }

    fun get(name: String): String {
        return env[name]!!
    }

    private fun loadProperties() {
        val paths = listOf(
            "src/main/resources/configuration.properties",
            "src/main/resources/application.properties",
            "configuration.properties",
            "application.properties",
            ".env"
        )

        paths.filter { Files.isRegularFile(Paths.get(it)) }
            .forEach { propertiesFile ->
                val properties: Properties = Properties().apply {
                    FileInputStream(propertiesFile).use { this.load(it) }
                }
                val keys: Set<String> = properties.stringPropertyNames()
                println("Found properties file: $propertiesFile")
                println("   keys: $keys")
                keys.forEach { key ->
                    val value = properties.getProperty(key).trim()
                    env[key] = value
                }
            }
        val sysEnv = System.getenv()
        env.putAll(sysEnv)
        println("Reading env.")
        println("   keys: ${sysEnv.keys}")

    }
}

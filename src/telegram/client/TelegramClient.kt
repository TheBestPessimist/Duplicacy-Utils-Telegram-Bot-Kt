package tbp.land

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.BrowserUserAgent
import io.ktor.client.features.UserAgent
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import java.time.Duration
import kotlin.system.measureTimeMillis


class TelegramBotClient(API_TOKEN: String) {

    val BASE_URL = "https://api.telegram.org/bot$API_TOKEN"

    private val client = HttpClient(Apache) {
        install(JsonFeature) {
            this.serializer = GsonSerializer()
        }
        install(Logging) {
            level = LogLevel.ALL
        }
        expectSuccess = false
        install(UserAgent)
    }


    fun getBotInfo() {
        val path = "/getWebhookInfo"
        doJsonRequest(path)
    }

    private fun doJsonRequest(path: String): String {
        var response: String = ""
        val millis = measureTimeMillis {
            response = runBlocking {
                val post = client.get<String>(BASE_URL + path) {
                    contentType(ContentType.Application.Json)
                }
                post
            }
        }
        println("Request duration: ${Duration.ofMillis(millis)} with response: $response")
        return response
    }
}

package tbp.land.telegram.client

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.UserAgent
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import tbp.land.telegram.client.dto.SendMessageJson
import tbp.land.telegram.client.dto.UpdateJson
import java.time.Duration
import kotlin.system.measureTimeMillis


class TelegramBotClient(TELEGRAM_API_TOKEN: String) {

    val BASE_URL = "https://api.telegram.org/bot$TELEGRAM_API_TOKEN"

    private val client = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = JacksonSerializer {
                enable(SerializationFeature.INDENT_OUTPUT)
                propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE

                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // similar to `configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)`

                // telegram uses time since epoch
                registerModule(JavaTimeModule()) // need to add this for jackson <-> java8 DateTime interop
                enable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                enable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
                //            disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                enable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
            }
        }


        install(Logging) {
            level = LogLevel.NONE
        }
        expectSuccess = false
        install(UserAgent)
    }

    /**
     * return the exact same message the user sent
     */
    fun echo(updateJson: UpdateJson) {
        val message =
            SendMessageJson(updateJson.message!!.chat.id, updateJson.message.text ?: "", updateJson.message.messageId)

//        println("the message is: $message")

        sendMessage(message)
    }

    private fun sendMessage(message: SendMessageJson) {
        val path = "/sendMessage"
        doJsonRequest(path, message)
    }

    fun getWebhookInfo() {
        val path = "/getWebhookInfo"
        doJsonRequest(path)
    }

    fun setWebhook(webhookAddress: String) {
        val path = "/setWebhook"
        // the link should be
        // https://api.telegram.org/bot_token/setWebhook?url=https://8db6966b.ngrok.io
        doJsonRequest("$path?url=$webhookAddress")
    }

    private fun doJsonRequest(path: String, message: SendMessageJson? = null): String {
        var response = ""
        val millis = measureTimeMillis {
            response = runBlocking {
                val post = client.post<String>(BASE_URL + path) {
                    contentType(ContentType.Application.Json)
                    if (message != null) {
                        this.body = message
                    }
                }
                post
            }
        }
//        println("Request duration: ${Duration.ofMillis(millis)} with response: $response")
        println("Request duration: ${Duration.ofMillis(millis)}")
        return response
    }
}

package tbp.land.telegram.client

import com.fasterxml.jackson.databind.ObjectMapper
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


class TelegramClient(API_TOKEN: String, private val objectMapperSettings: ObjectMapper.() -> Unit) {

    @Suppress("PrivatePropertyName")
    private val TELEGRAM_API_URL = "https://api.telegram.org/bot$API_TOKEN"

    private val client = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = JacksonSerializer(objectMapperSettings)
        }

        install(Logging) {
            level = LogLevel.NONE
        }

        expectSuccess = false

        install(UserAgent) {
            agent = "@TheBestPessimist's Telegram bot with Ktor"
        }
    }

    /**
     * return the exact same message the user sent
     */
    suspend fun echo(updateJson: UpdateJson) {
        val message =
            SendMessageJson(updateJson.message!!.chat.id, updateJson.message.text ?: "", updateJson.message.messageId)

        println("the message is: $message")

        sendMessage(message)
    }

    private suspend fun sendMessage(message: SendMessageJson) {
        val path = "/sendMessage"
        doJsonRequest(path, message)
    }

    @Suppress("unused")
    suspend fun getWebhookInfo() {
        val path = "/getWebhookInfo"
        val webhookInfo = doJsonRequest(path)
        println(webhookInfo)
    }

    internal fun setWebhook(webhookAddress: String) {
        runBlocking {
            val path = "/setWebhook"
            // the link should be
            // https://api.telegram.org/bot_token/setWebhook?url=https://8db6966b.ngrok.io
            val response = doJsonRequest("$path?url=$webhookAddress")
            println(response)
        }
    }

    private suspend fun doJsonRequest(path: String, message: SendMessageJson? = null): String {
        var response = ""
        response = runBlocking {
            val post = client.post<String>(TELEGRAM_API_URL + path) {
                contentType(ContentType.Application.Json)
                if (message != null) {
                    this.body = message
                    println("doJsonRequest with: $message")
                }
            }
            post
        }
//        println("Request duration: ${Duration.ofMillis(millis)} with response: $response")
        return response
    }
}

package tbp.land.telegram.client

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.UserAgent
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import tbp.land.telegram.client.dto.JsonSendMessage


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

    suspend fun sendMessage(message: JsonSendMessage) {
        val path = "/sendMessage"
        doJsonRequest(path, message)
    }

    suspend fun getWebhookInfo() {
        val path = "/getWebhookInfo"
        doJsonRequest(path)
    }

    internal fun setWebhook(webhookAddress: String, certificateData: ByteArray?) {
        runBlocking {
            val path = "/setWebhook"

            val response: String = client.post("$TELEGRAM_API_URL$path") {
                body = MultiPartFormDataContent(
                    formData {
                        this.append("url", webhookAddress)
                        if (certificateData != null) {
                            this.append("certificate", certificateData)
                        }
                    })
            }
            println("webhook response: $response")
        }
    }


    private suspend fun doJsonRequest(path: String, message: JsonSendMessage? = null): String {
        val response: String = client.post(TELEGRAM_API_URL + path) {
            contentType(ContentType.Application.Json)
            if (message != null) {
                this.body = message
                println("doJsonRequest $path with: $message")
            }
        }
        println("$path: $response")

        return response
    }
}

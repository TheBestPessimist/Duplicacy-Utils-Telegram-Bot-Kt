package tbp.land.telegram.client

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import tbp.land.telegram.client.dto.JsonSendMessage


class TelegramClient(API_TOKEN: String, private val objectMapperSettings: ObjectMapper.() -> Unit) {

    @Suppress("PrivatePropertyName")
    private val TELEGRAM_API_URL = "https://api.telegram.org/bot$API_TOKEN"

    private val client = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = JacksonSerializer(block = objectMapperSettings)
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

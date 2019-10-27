package tbp.land.telegram

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.coroutines.runBlocking
import tbp.land.tbp.land.notification.JsonBackupNotification
import tbp.land.telegram.Telegram.Companion.TELEGRAM_UPDATE_WEBHOOK_ROUTE
import tbp.land.telegram.client.TelegramClient
import tbp.land.telegram.client.dto.JsonSendMessage
import tbp.land.telegram.client.dto.JsonUpdate

class Telegram constructor(private val botApiToken: String, private val serverAddress: String) {
    internal val client: TelegramClient by lazyTelegramClientInit()

    internal val objectMapperSettings: ObjectMapper.() -> Unit = {
        enable(SerializationFeature.INDENT_OUTPUT)
        propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE

        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // similar to `configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)`

        // telegram uses time since epoch
        registerModule(JavaTimeModule()) // need to add this for jackson <-> java8 DateTime interop
        enable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
        enable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
        enable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
    }

    internal companion object {
        const val TELEGRAM_UPDATE_WEBHOOK_ROUTE = "/telegram/TELEGRAM_UPDATE_WEBHOOK_ROUTE"
    }

    /**
     * There's no benefit in making the client lazy.
     * I wanted to see how a lazy method looks like since my aim here is to learn Kotlin.
     */
    private fun lazyTelegramClientInit(): Lazy<TelegramClient> {
        return lazy {
            val a = TelegramClient(botApiToken, objectMapperSettings)
            a.setWebhook(serverAddress + TELEGRAM_UPDATE_WEBHOOK_ROUTE)
            println("in client lazy")
            a
        }
    }
}

fun Telegram.installRoutes(application: Application) {
    fun Routing.handleIncomingUpdateFromTelegram() {
        post(TELEGRAM_UPDATE_WEBHOOK_ROUTE) {
            val update: JsonUpdate = call.receive()
            val response = constructDefaultMessage(update)
            client.sendMessage(response)
            call.respond(HttpStatusCode.OK)
        }
    }

    application.apply {
        install(ContentNegotiation) {
            jackson(block = objectMapperSettings)
        }

        routing {
            handleIncomingUpdateFromTelegram()
        }
    }

    runBlocking {
        // need this to lazily init the client
        client.getWebhookInfo()
    }
}

suspend fun Telegram.forwardBackupNotification(backupNotification: JsonBackupNotification) {
    val message = JsonSendMessage(backupNotification.chatId, backupNotification.content)
    client.sendMessage(message)
}

private fun constructDefaultMessage(
    update: JsonUpdate
): JsonSendMessage {
    val msg = """
            |This bot is a simple one.
            |
            |Its purpose is to message you whenever a backup has started or finished as long as you use @TheBestPessimist's <a href='https://git.tbp.land/duplicacy-utils/'>duplicacy utils</a>.
            |
            |Here's what you need to paste in the user config:
            |
            |<code>${'$'}telegramToken = ${update.message!!.chat.id}</code>
            """.trimMargin()

    return JsonSendMessage(update.message.chat.id, msg, update.message.messageId)
}

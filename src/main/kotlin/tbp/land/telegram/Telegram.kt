package tbp.land.telegram

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import tbp.land.notification.JsonBackupNotification
import tbp.land.telegram.client.TelegramClient
import tbp.land.telegram.client.dto.JsonSendMessage
import tbp.land.telegram.client.dto.JsonUpdate
import java.nio.file.Files
import java.nio.file.Paths

class Telegram(
    private val botApiToken: String,
    private val serverAddress: String,
    internal val webhookRoute: String,
    private val certificatePath: String
) {
    internal val client: TelegramClient by lazyTelegramClientInit()

    internal val objectMapperSettings: ObjectMapper.() -> Unit = {
        enable(SerializationFeature.INDENT_OUTPUT)
        propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE

        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // similar to `configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)`

        // telegram uses time since epoch
        registerModule(JavaTimeModule()) // need to add this for jackson <-> java8 DateTime interop
        enable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
        enable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
        enable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
    }

    /**
     * There's no benefit in making the client lazy.
     * I wanted to see how a lazy method looks like since my aim here is to learn Kotlin.
     */
    private fun lazyTelegramClientInit(): Lazy<TelegramClient> {
        return lazy {
            val c = TelegramClient(
                botApiToken,
                objectMapperSettings
            )
            c.setWebhook(serverAddress + webhookRoute, readCertificate(certificatePath))
            println("in client lazy")
            c
        }
    }

    private fun readCertificate(stringPath: String): ByteArray? {
        val path = Paths.get(stringPath)

        return if (Files.isRegularFile(path)) {
            Files.readAllBytes(path)
        } else {
            null
        }
    }
}

fun Telegram.initializeTelegramClient(application: Application) {
    fun Routing.handleIncomingUpdateFromTelegram() {
        post(webhookRoute) {
            val update: JsonUpdate = call.receive()
            val response = constructDefaultMessage(update)
            client.sendMessage(response)
            call.respond(HttpStatusCode.OK)
        }
    }

    application.apply {
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
    val message = JsonSendMessage(
        backupNotification.chatId,
        backupNotification.content
    )
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

    return JsonSendMessage(
        update.message.chat.id,
        msg,
        update.message.messageId
    )
}

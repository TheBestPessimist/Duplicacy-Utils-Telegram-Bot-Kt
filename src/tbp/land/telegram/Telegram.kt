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
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.coroutines.runBlocking
import tbp.land.telegram.Telegram.Companion.TELEGRAM_UPDATE_WEBHOOK_PATH
import tbp.land.telegram.client.TelegramClient
import tbp.land.telegram.client.dto.UpdateJson

class Telegram constructor(private val botApiToken: String, private val serverAddress: String) {

    val client: TelegramClient by lazyTelegramClientInit()

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


    companion object {
        const val TELEGRAM_UPDATE_WEBHOOK_PATH = "/telegram/TELEGRAM_UPDATE_WEBHOOK_PATHsffddda"
    }


    private fun lazyTelegramClientInit(): Lazy<TelegramClient> {
        return lazy {
            val a = TelegramClient(botApiToken, objectMapperSettings)
            a.setWebhook(serverAddress + TELEGRAM_UPDATE_WEBHOOK_PATH)
            println("in client lazy")
            a
        }
    }
}

fun Telegram.installRoutes(application: Application) {
    fun Routing.dummy() {
        get("/t") {
            call.respond("t babyyyy")
        }
    }

    fun Routing.webhookEcho() {
        post(TELEGRAM_UPDATE_WEBHOOK_PATH) {
            val update: UpdateJson = call.receive()
            println(update)
            client.echo(update)
            call.respond(HttpStatusCode.OK)
        }
    }




    application.apply {
        install(ContentNegotiation) {
            jackson(block = objectMapperSettings)
        }

        routing {
            dummy()
            webhookEcho()
        }
    }


    runBlocking {
        // need this to lazily init the client
        client.getWebhookInfo()
    }

}

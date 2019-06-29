package tbp.land

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.AutoHeadResponse
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import tbp.land.telegram.client.TelegramBotClient
import tbp.land.telegram.server.telegramPostUpdateWebhookEndpoint

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)

    val telegramBotClient = TelegramBotClient(TheConfig.TELEGRAM_API_TOKEN)
    telegramBotClient.setWebhook(TheConfig.WEBSERVER_ADDRESS + TheConfig.TELEGRAM_UPDATE_WEBHOOK_PATH)

//    while (true) {
//        TelegramBotClient(TheConfig.TELEGRAM_API_TOKEN).getWebhookInfo()
//        TimeUnit.SECONDS.sleep(10)
//    }
}


@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.a(testing: Boolean = false) {
//    val client = HttpClient() {
//        install(JsonFeature) {
//            serializer = GsonSerializer()
//        }
//        install(Logging) {
//            level = LogLevel.ALL
//        }
////        BrowserUserAgent() // install default browser-like user-agent
//        install(UserAgent) { this.agent = "TBP user agent" }
//    }
//    runBlocking {
//        // Sample for making a HTTP Client request
//        val message = client.post<JsonSampleClass> {
//            url("http://127.0.0.1:8080/path/to/endpoint")
//            contentType(ContentType.Application.Json)
//            body = JsonSampleClass(hello = "world")
//        }
//    }

    install(ContentNegotiation) {
        jackson {
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

//    install(Compression) {
//        gzip {
//            priority = 1.0
//        }
//        deflate {
//            priority = 10.0
//            minimumSize(1024) // condition
//        }
//    }

    install(AutoHeadResponse)

    install(CallLogging) {
        //        level = Level.INFO
//        filter { call -> call.request.path().startsWith("/") }
    }

    install(DefaultHeaders) {
        header("X-Engine", "@TheBestPessimist's Telegram bot with Ktor") // will send this header with each response
    }

//    install(ForwardedHeaderSupport) // WARNING: for security, do not include this if not behind a reverse proxy
//    install(XForwardedHeaderSupport) // WARNING: for security, do not include this if not behind a reverse proxy

//    install(HSTS) {
//        includeSubDomains = true
//    }

    routing {
        rootPath()

        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }



        telegramPostUpdateWebhookEndpoint()

    }
}


private fun Routing.rootPath() {
    get("/") {
        call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
    }
}


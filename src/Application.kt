package tbp.land

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.application.Application
import io.ktor.application.ApplicationCallPipeline
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
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import tbp.land.telegram.Telegram
import tbp.land.telegram.enlistTelegram
import java.time.Duration
import kotlin.system.measureTimeMillis

val telegram = Telegram(TheConfig.TELEGRAM_API_TOKEN, TheConfig.WEBSERVER_ADDRESS)

fun main(args: Array<String>) {
//    io.ktor.server.netty.EngineMain.main(args)


    val embeddedServer: NettyApplicationEngine = embeddedServer(Netty, 13337) {
//        addARoute(this)
//        a()
//        telegramModule()
        telegram.enlistTelegram(this)

    }

    embeddedServer.start(true)
}

fun addARoute(application: Application) {
    application.a()

//    get("/") {
//        call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
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


    routing {

        get("/hello") {
            call.respond(mapOf("hello" to "world"))
        }

        measureTime {
            rootPath()
        }
    }



}


fun Routing.measureTime(routesForMeasurement: Route.() -> Unit): Route {
    val routeWithTimeMeasuredNode = this.createChild(object : RouteSelector(RouteSelectorEvaluation.qualityConstant) {
        override fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation =
            RouteSelectorEvaluation.Constant
    })


    routeWithTimeMeasuredNode.intercept(ApplicationCallPipeline.Monitoring) { context ->
        val millis = measureTimeMillis {
            proceed()
        }
        println("Request duration: ${Duration.ofMillis(millis)}")
    }

    routesForMeasurement(routeWithTimeMeasuredNode)
    return routeWithTimeMeasuredNode
}


private fun Route.rootPath() {
    get("/") {
        call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
    }
}


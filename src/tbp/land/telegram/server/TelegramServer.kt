package tbp.land.telegram.server

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import tbp.land.TheConfig
import tbp.land.telegram.client.TelegramBotClient
import tbp.land.telegram.client.dto.UpdateJson


fun Routing.telegramPostUpdateWebhookEndpoint() {
    post(TheConfig.TELEGRAM_UPDATE_WEBHOOK_PATH) {
        call.respond(HttpStatusCode.OK)

//        val receiveText = call.receiveText()
//        println("text: $receiveText")
//
//        // here i'm just trying to search for jackson's object mapper
////        this.application.feature(ContentNegotiation).registrations.takeIf { it. }
//
//        val m = jacksonObjectMapper()
//            .enable(SerializationFeature.INDENT_OUTPUT)
//            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
//            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // similar to `configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)`
//            .registerModule(JavaTimeModule()) // need to add this for jackson <-> java8 DateTime interop
//            .enable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
//            .enable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
//            .enable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)!!
//
//        val update: UpdateJson = m.readValue(receiveText)
//        println("jacksoned: $update")

        val update: UpdateJson = call.receive()
//        println(update)
//
        val telegramBotClient = TelegramBotClient(TheConfig.TELEGRAM_API_TOKEN)
        telegramBotClient.echo(update)

//        val date = update.message?.date!!
//        println(date)
//        println(date.toInstant())
//        println(date.withZoneSameInstant(ZoneId.systemDefault()))
    }
}

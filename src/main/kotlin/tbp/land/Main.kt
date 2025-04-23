package tbp.land

import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import org.slf4j.event.Level
import tbp.land.notification.BACKUP_NOTIFICATION_ROUTE_1
import tbp.land.notification.BACKUP_NOTIFICATION_ROUTE_2
import tbp.land.notification.backupNotificationRoute
import tbp.land.telegram.Telegram
import tbp.land.telegram.initializeTelegramClient

val telegram = Telegram(
    TelegramConfig.TELEGRAM_API_TOKEN,
    TelegramConfig.WEBSERVER_ADDRESS,
    TelegramConfig.WEBHOOK_ROUTE,
    TelegramConfig.CERTIFICATE_PATH
)

fun main() {
    val embeddedServer = embeddedServer(Netty, 13337) {
        telegram.initializeTelegramClient(this)

        install(ContentNegotiation) {
            jackson(block = telegram.objectMapperSettings)
        }

        install(CallLogging) {
            level = Level.TRACE
        }

        routing {
            backupNotificationRoute(BACKUP_NOTIFICATION_ROUTE_1, telegram)
            backupNotificationRoute(BACKUP_NOTIFICATION_ROUTE_2, telegram)
        }
    }

    embeddedServer.start(true)
}

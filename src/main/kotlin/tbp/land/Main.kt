package tbp.land

import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import tbp.land.main.kotlin.TelegramConfig
import tbp.land.notification.BACKUP_NOTIFICATION_ROUTE_1
import tbp.land.notification.BACKUP_NOTIFICATION_ROUTE_2
import tbp.land.notification.backupNotificationRoute
import tbp.land.telegram.Telegram
import tbp.land.telegram.initializeTelegramClient

val telegram = Telegram(
    TelegramConfig.TELEGRAM_API_TOKEN,
    TelegramConfig.WEBSERVER_ADDRESS,
    TelegramConfig.CERTIFICATE_PATH
)

fun main() {
    val embeddedServer: NettyApplicationEngine = embeddedServer(Netty, 13337) {
        telegram.initializeTelegramClient(this)

        install(ContentNegotiation) {
            jackson(block = telegram.objectMapperSettings)
        }

        routing {
            backupNotificationRoute(BACKUP_NOTIFICATION_ROUTE_1, telegram)
            backupNotificationRoute(BACKUP_NOTIFICATION_ROUTE_2, telegram)
        }
    }

    embeddedServer.start(true)
}

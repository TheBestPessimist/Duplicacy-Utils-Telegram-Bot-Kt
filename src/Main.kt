package tbp.land

import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import tbp.land.tbp.land.notification.BACKUP_NOTIFICATION_ROUTE_1
import tbp.land.tbp.land.notification.BACKUP_NOTIFICATION_ROUTE_2
import tbp.land.tbp.land.notification.backupNotificationRoute
import tbp.land.telegram.Telegram
import tbp.land.telegram.installRoutes

val telegram = Telegram(TheConfig.TELEGRAM_API_TOKEN, TheConfig.WEBSERVER_ADDRESS)

fun main() {
    val embeddedServer: NettyApplicationEngine = embeddedServer(Netty, 13337) {
        telegram.installRoutes(this)

        routing {
            backupNotificationRoute(BACKUP_NOTIFICATION_ROUTE_1, telegram)
            backupNotificationRoute(BACKUP_NOTIFICATION_ROUTE_2, telegram)
        }
    }

    embeddedServer.start(true)
}

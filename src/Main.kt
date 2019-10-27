package tbp.land

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import tbp.land.telegram.Telegram
import tbp.land.telegram.installRoutes

val telegram = Telegram(TheConfig.TELEGRAM_API_TOKEN, TheConfig.WEBSERVER_ADDRESS)

fun main() {
    val embeddedServer: NettyApplicationEngine = embeddedServer(Netty, 13337) {
        telegram.installRoutes(this)
    }

    embeddedServer.start(true)
}

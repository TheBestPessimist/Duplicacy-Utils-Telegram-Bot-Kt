package tbp.land

import tbp.land.env.Env

object TelegramConfig {
    val TELEGRAM_API_TOKEN: String by lazy { Env.get("TELEGRAM_API_TOKEN") }
    val WEBSERVER_ADDRESS: String by lazy { Env.get("WEBSERVER_ADDRESS") }
    val WEBHOOK_ROUTE: String by lazy { Env.get("WEBHOOK_ROUTE") }
    val CERTIFICATE_PATH: String by lazy { Env.get("CERTIFICATE_PATH") }
}

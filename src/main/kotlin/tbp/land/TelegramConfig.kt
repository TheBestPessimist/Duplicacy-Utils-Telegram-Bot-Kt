package tbp.land

import tbp.land.env.Env

object TelegramConfig {
    val TELEGRAM_API_TOKEN: String by lazy { Env.get("telegram_api_token") }
    val WEBSERVER_ADDRESS: String by lazy { Env.get("webserver_address") }
    val CERTIFICATE_PATH: String by lazy { Env.get("certificate_path") }
}

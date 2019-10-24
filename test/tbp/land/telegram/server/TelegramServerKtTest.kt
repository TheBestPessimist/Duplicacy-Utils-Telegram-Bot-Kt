package tbp.land.telegram.server

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.intellij.lang.annotations.Language
import org.junit.Ignore
import org.junit.Test
import tbp.land.TheConfig
import tbp.land.a

internal class TelegramServerKtTest {

    @Test
    @Ignore
    fun telegramPostUpdateWebhookEndpoint() {

        withTestApplication({ a(testing = true) }) {
            handleRequest(HttpMethod.Get, "/").apply {
                kotlin.test.assertEquals(HttpStatusCode.OK, response.status())
                kotlin.test.assertEquals("HELLO WORLD!", response.content)
            }
        }
    }

    @Test
    fun anotherTest() {
        @Language("JSON")
        val a = """
{
  "update_id": 1372626,
  "message": {
    "message_id": 70,
    "from": {
      "id": 187695476,
      "is_bot": false,
      "first_name": "TheBestPessimist",
      "username": "TheBestPessimist",
      "language_code": "en"
    },
    "chat": {
      "id": 187695476,
      "first_name": "TheBestPessimist",
      "username": "TheBestPessimist",
      "type": "private"
    },
    "date": 1561804376,
    "text": "123456"
  }
}
 """

//        withTestApplication({ a(testing = true) }) {
//            handleRequest(HttpMethod.Post, TheConfig.TELEGRAM_UPDATE_WEBHOOK_PATH) {
//                this.setBody(a)
//                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
//            }.apply {
//                println("${response.content}")
//            }
//        }

    }
}





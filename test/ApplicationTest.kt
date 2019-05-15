package tbp.land

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.UserAgent
import io.ktor.client.features.BrowserUserAgent
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*
import io.ktor.jackson.*
import io.ktor.features.*
import org.slf4j.event.*
import kotlin.test.*
import io.ktor.server.testing.*
import io.ktor.client.engine.mock.*
import kotlinx.coroutines.io.*
import io.ktor.client.call.*

class ApplicationTest {
    @Test
    fun testClientMock() {
//        runBlocking {
//            val client = HttpClient(MockEngine {
//                if (it.url.encodedPath == "/") {
//                    MockHttpResponse(it.call, HttpStatusCode.OK, ByteReadChannel(byteArrayOf(1, 2, 3)), headersOf("X-MyHeader", "MyValue"))
//                } else {
//                    responseError(HttpStatusCode.NotFound, "Not Found ${it.url.encodedPath}")
//                }
//            }) {
//                expectSuccess = false
//            }
//            assertEquals(byteArrayOf(1, 2, 3).toList(), client.get<ByteArray>("/").toList())
//            assertEquals("MyValue", client.call("/").response.headers["X-MyHeader"])
//            assertEquals("Not Found other/path", client.get<String>("/other/path"))
//        }
    }

    @Test
    fun testRoot() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("HELLO WORLD!", response.content)
            }
        }
    }
}

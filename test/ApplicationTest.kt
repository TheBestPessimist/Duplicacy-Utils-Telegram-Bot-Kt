package tbp.land

import kotlin.test.Test

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

//    @Test
//    fun testRoot() {
//        withTestApplication({ a(testing = true) }) {
//            handleRequest(HttpMethod.Get, "/").apply {
//                assertEquals(HttpStatusCode.OK, response.status())
//                assertEquals("HELLO WORLD!", response.content)
//            }
//        }
//    }
}





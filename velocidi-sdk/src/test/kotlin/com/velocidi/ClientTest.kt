package com.velocidi

import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import com.velocidi.util.containsBody
import com.velocidi.util.containsHeader
import com.velocidi.util.containsRequestLine
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ClientTest {

    var server = MockWebServer()
    val url = server.url("/")
    var client = HttpClient()

    @Rule @JvmField
    var globalTimeout = Timeout.seconds(10) // 10 seconds max per method tested

    @Test
    fun emptyRequest() {
        server.enqueue(MockResponse())

        client.sendRequest(HttpClient.Verb.GET, url.url())
        val response1 = server.takeRequest()
        response1.containsRequestLine("GET / HTTP/1.1")
        assertThat(String(response1.body.readByteArray())).isEmpty()

        client.sendRequest(HttpClient.Verb.POST, url.url())
        val response2 = server.takeRequest()
        response2.containsRequestLine("POST / HTTP/1.1")
        assertThat(String(response2.body.readByteArray())).isEmpty()
    }

    @Test
    fun customHeaderRequest() {
        server.enqueue(MockResponse())

        client.sendRequest(HttpClient.Verb.GET, url.url(), headers = mapOf("User-Agent" to "CustomUA"))

        val response = server.takeRequest()
        response.containsHeader("User-Agent", "CustomUA")
    }

    @Test
    fun payloadRequest() {
        server.enqueue(MockResponse())

        val payload = """{"Hello":"World"}"""

        client.sendRequest(HttpClient.Verb.POST, url.url(), JSONObject(payload))
        val response = server.takeRequest()
        response.containsHeader("Content-Type", "application/json")
        response.containsBody(payload)
    }

    @Test
    fun queueRequests() {
        server.enqueue(MockResponse())

        client.sendRequest(HttpClient.Verb.GET, url.url())
        client.sendRequest(HttpClient.Verb.GET, url.url())
        client.sendRequest(HttpClient.Verb.GET, url.url())

        assertThat(server.requestCount).isEqualTo(0)

        server.takeRequest()
        server.takeRequest()
        server.takeRequest()

        assertThat(client.requestQueue.sequenceNumber).isEqualTo(4)
        assertThat(server.requestCount).isEqualTo(3)
    }

    @Test
    fun queryParams() {
        server.enqueue(MockResponse())

        client.sendRequest(HttpClient.Verb.POST, url.url(), parameters = mapOf("x" to "foo", "y" to "bar"))

        val response = server.takeRequest()

        response.containsRequestLine("POST /?x=foo&y=bar HTTP/1.1")
    }
}

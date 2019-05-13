package com.velocidi

import android.net.Uri
import com.google.gson.*
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import com.velocidi.util.containsBody
import com.velocidi.util.containsHeader
import com.velocidi.util.containsRequestLine
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HttpClientTest {

    var server = MockWebServer()
    val url = Uri.parse(server.url("/").toString())
    private var client = HttpClient()

    // @Rule
    // @JvmField
    // val globalTimeout = Timeout.seconds(10) // 10 seconds max per method tested

    @Test
    fun emptyRequest() {
        server.enqueue(MockResponse())

        client.sendRequest(HttpClient.Verb.GET, url)
        val response1 = server.takeRequest()
        response1.containsRequestLine("GET / HTTP/1.1")
        assertThat(String(response1.body.readByteArray())).isEmpty()

        client.sendRequest(HttpClient.Verb.POST, url)
        val response2 = server.takeRequest()
        response2.containsRequestLine("POST / HTTP/1.1")
        assertThat(String(response2.body.readByteArray())).isEmpty()
    }

    @Test
    fun customHeaderRequest() {
        server.enqueue(MockResponse())

        client.sendRequest(
            HttpClient.Verb.GET,
            url,
            headers = mapOf("User-Agent" to "CustomUA")
        )

        val response = server.takeRequest()
        response.containsHeader("User-Agent", "CustomUA")
    }

    @Test
    fun payloadRequest() {
        server.enqueue(MockResponse())

        val payload = """{"Hello":"World"}"""

        client.sendRequest(HttpClient.Verb.POST, url, JSONObject(payload))
        val response = server.takeRequest()
        response.containsHeader("Content-Type", "application/json; charset=utf-8")
        response.containsBody(payload)
    }

    @Test
    fun queryParams() {
        server.enqueue(MockResponse())

        client.sendRequest(
            HttpClient.Verb.POST, url,
            parameters = mapOf(
                "x" to "foo",
                "y" to "bar"
            )
        )

        val response = server.takeRequest()

        response.containsRequestLine("POST /?x=foo&y=bar HTTP/1.1")
    }
}

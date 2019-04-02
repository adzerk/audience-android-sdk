package com.velocidi

import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import com.velocidi.util.containsRequestLine
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.net.URL
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
class VelocidiTest {

    var server = MockWebServer()

    //@Rule @JvmField
    //var globalTimeout = Timeout.seconds(10) // 5 seconds max per method tested


    @Test
    fun configSDK(){
        val config = Config(URL("http://example.com"))

        assertThat(config.track.host.toString()).isEqualTo("http://tr.example.com")
        assertThat(config.track.enabled).isTrue()
        assertThat(config.match.host.toString()).isEqualTo("http://match.example.com")
        assertThat(config.match.enabled).isTrue()
    }

    @Test
    fun trackingEvent(){
        val url = server.url("/tr")
        val config = Config(Channel(URL(url.toString()), true), Channel(URL(url.toString()), false))

        val event = """
            {
                "eventType":"pageView",
                "clientId": "client1",
                "siteId": "site1"
            }
            """

        Velocidi.start(config)

        server.enqueue(MockResponse())
        Velocidi.track(JSONObject(event))
        val response = server.takeRequest()
        response.containsRequestLine("POST /tr HTTP/1.1")
    }

    @Test
    fun trackingEventDisabled(){
        val url = server.url("/tr")
        val config = Config(Channel(URL(url.toString()), false), Channel(URL(url.toString()), false))

        val event = """
            {
                "eventType":"pageView",
                "clientId": "client1",
                "siteId": "site1"
            }
            """

        Velocidi.start(config)
        Velocidi.track(JSONObject(event))
        val response = server.takeRequest(2, TimeUnit.SECONDS)
        assertThat(response).isNull()
    }

    @Test
    fun matchEvent(){
        val url = server.url("/match")
        val config = Config(Channel(URL(url.toString()), false), Channel(URL(url.toString()), true))

        Velocidi.start(config)
        Velocidi.match("provider1", listOf(UserId("eml","mail@example.com")))
        val response = server.takeRequest()
        response.containsRequestLine("GET /match?providerId=provider1&id_eml=mail@example.com HTTP/1.1")
    }

    @Test
    fun matchEventDisabled(){
        val url = server.url("/match")
        val config = Config(Channel(URL(url.toString()), false), Channel(URL(url.toString()), false))

        Velocidi.start(config)
        Velocidi.match("provider1", listOf(UserId("eml","mail@example.com")))
        val response = server.takeRequest(2, TimeUnit.SECONDS)
        assertThat(response).isNull()
    }
}

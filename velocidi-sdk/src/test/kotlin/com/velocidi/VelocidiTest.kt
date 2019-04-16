package com.velocidi

// @Rule
// @JvmField

import android.content.Context
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import com.velocidi.util.containsRequestLine
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.net.URL
import java.util.concurrent.TimeUnit

internal class VelocidiMock(
    config: Config,
    context: Context,
    advertisingInfo: AdvertisingInfo = AdvertisingInfo("123", true)
) : Velocidi(config, context) {

    init {
        this.adInfo = advertisingInfo
    }

    override fun fetchAndSetAdvertisingId(context: Context) {
        return
    }
}

@RunWith(RobolectricTestRunner::class)
class VelocidiTest {

    var server = MockWebServer()

    // var globalTimeout = Timeout.seconds(10) // 10 seconds max per method tested

    @Test
    fun configSDK() {
        val config = Config(URL("http://example.com"))

        assertThat(config.track.host.toString()).isEqualTo("http://tr.example.com")
        assertThat(config.track.enabled).isTrue()
        assertThat(config.match.host.toString()).isEqualTo("http://match.example.com")
        assertThat(config.match.enabled).isTrue()
    }

    @Test
    fun trackingEvent() {
        val url = server.url("/tr")
        val config = Config(Channel(URL(url.toString()), true), Channel(URL(url.toString()), false))

        val event = """
            {
                "eventType":"pageView",
                "clientId": "client1",
                "siteId": "site1"
            }
            """

        val context = RuntimeEnvironment.application

        Velocidi.instance = VelocidiMock(config, context)

        server.enqueue(MockResponse())
        Velocidi.getInstance().track(JSONObject(event))
        val response = server.takeRequest()
        response.containsRequestLine("POST /tr?cookies=false&id_aaid=123 HTTP/1.1")
    }

    @Test
    fun trackingEventDisabled() {
        val url = server.url("/tr")
        val config = Config(
            Channel(URL(url.toString()), false),
            Channel(URL(url.toString()), false)
        )

        val event = """
            {
                "eventType":"pageView",
                "clientId": "client1",
                "siteId": "site1"
            }
            """

        val context = RuntimeEnvironment.application

        Velocidi.instance = VelocidiMock(config, context)

        Velocidi.getInstance().track(JSONObject(event))
        val response = server.takeRequest(2, TimeUnit.SECONDS)
        assertThat(response).isNull()
    }

    @Test
    fun matchEvent() {
        val url = server.url("/match")
        val config = Config(Channel(URL(url.toString()), false), Channel(URL(url.toString()), true))

        val context = RuntimeEnvironment.application

        Velocidi.instance = VelocidiMock(config, context)

        Velocidi.getInstance().match("provider1", listOf(UserId("eml", "mail@example.com")))
        val response = server.takeRequest()
        response.containsRequestLine("GET /match?providerId=provider1&id_eml=mail@example.com&cookies=false&id_aaid=123 HTTP/1.1")
    }

    @Test
    fun matchEventDisabled() {
        val url = server.url("/match")
        val config = Config(
            Channel(URL(url.toString()), false),
            Channel(URL(url.toString()), false)
        )

        val context = RuntimeEnvironment.application
        Velocidi.instance = VelocidiMock(config, context)

        Velocidi.getInstance().match("provider1", listOf(UserId("eml", "mail@example.com")))
        val response = server.takeRequest(2, TimeUnit.SECONDS)
        assertThat(response).isNull()
    }

    @Test
    @Ignore
    fun accumulateRequestWhileAaidUndefined() {

        val url = server.url("/")

        val config = Config(
            Channel(URL(url.toString()), false),
            Channel(URL(url.toString()), false)
        )

        val context = RuntimeEnvironment.application
        val instance = VelocidiMock(config, context)

        instance.match("provider1", listOf(UserId("eml", "mail@example.com")))
        instance.match("provider1", listOf(UserId("eml", "mail@example.com")))
        instance.match("provider1", listOf(UserId("eml", "mail@example.com")))

        assertThat(instance.queue.size).isEqualTo(3)
    }
    @Test
    fun trackingDisabled() {
        val url = server.url("/")
        val config = Config(Channel(URL(url.toString()), true), Channel(URL(url.toString()), true))

        val context = RuntimeEnvironment.application

        Velocidi.instance = VelocidiMock(config, context, AdvertisingInfo("123", false))

        Velocidi.getInstance().match("provider1", listOf(UserId("eml", "mail@example.com")))
        Velocidi.getInstance().track(JSONObject())
        val response = server.takeRequest(2, TimeUnit.SECONDS)
        assertThat(response).isNull()
    }
}

package com.velocidi

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import com.velocidi.util.containsRequestLine
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
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

    @Rule
    @JvmField
    val globalTimeout = Timeout.seconds(10) // 10 seconds max per method tested

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

        val context: Context = ApplicationProvider.getApplicationContext()

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

        val context: Context = ApplicationProvider.getApplicationContext()

        Velocidi.instance = VelocidiMock(config, context)

        Velocidi.getInstance().track(JSONObject(event))
        val response = server.takeRequest(2, TimeUnit.SECONDS)
        assertThat(response).isNull()
    }

    @Test
    fun matchEvent() {
        val url = server.url("/match")
        val config = Config(Channel(URL(url.toString()), false), Channel(URL(url.toString()), true))

        val context: Context = ApplicationProvider.getApplicationContext()

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

        val context: Context = ApplicationProvider.getApplicationContext()
        Velocidi.instance = VelocidiMock(config, context)

        Velocidi.getInstance().match("provider1", listOf(UserId("eml", "mail@example.com")))
        val response = server.takeRequest(2, TimeUnit.SECONDS)
        assertThat(response).isNull()
    }

    @Test
    fun shouldNotTrackUser() {
        val url = server.url("/")
        val config = Config(Channel(URL(url.toString()), true), Channel(URL(url.toString()), true))

        val context: Context = ApplicationProvider.getApplicationContext()

        Velocidi.instance = VelocidiMock(config, context, AdvertisingInfo("123", false))

        Velocidi.getInstance().match("provider1", listOf(UserId("eml", "mail@example.com")))
        Velocidi.getInstance().track(JSONObject())
        val response = server.takeRequest(2, TimeUnit.SECONDS)
        assertThat(response).isNull()
    }
}

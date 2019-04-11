package com.velocidi

import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import com.velocidi.events.PageView
import com.velocidi.util.containsRequestLine
import kotlinx.serialization.ImplicitReflectionSerializer
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.net.URL
import java.util.concurrent.TimeUnit


@RunWith(RobolectricTestRunner::class)
class VelocidiTest {

    var server = MockWebServer()

    @Rule
    @JvmField
    var globalTimeout = Timeout.seconds(10) // 10 seconds max per method tested


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

        val context = RuntimeEnvironment.application

        Velocidi.instance =  Velocidi(config, AdvertisingInfo("123", true), context)

        server.enqueue(MockResponse())
        Velocidi.track(PageView("site1", "clientId1"))
        val response = server.takeRequest()
        response.containsRequestLine("POST /tr?aaid=123&cookies=false HTTP/1.1")
    }

    @Test
    fun trackingEventDisabled(){
        val url = server.url("/tr")
        val config = Config(Channel(URL(url.toString()), false), Channel(URL(url.toString()), false))

        val context = RuntimeEnvironment.application

        Velocidi.instance =  Velocidi(config, AdvertisingInfo("123", true), context)

        Velocidi.track(PageView("site1", "clientId1"))
        val response = server.takeRequest(2, TimeUnit.SECONDS)
        assertThat(response).isNull()
    }

    @Test
    fun matchEvent(){
        val url = server.url("/match")
        val config = Config(Channel(URL(url.toString()), false), Channel(URL(url.toString()), true))

        val context = RuntimeEnvironment.application
        Velocidi.instance =  Velocidi(config, AdvertisingInfo("123", true), context)

        Velocidi.match("provider1", listOf(UserId("eml","mail@example.com")))
        val response = server.takeRequest()
        response.containsRequestLine("GET /match?providerId=provider1&id_eml=mail@example.com&aaid=123&cookies=false HTTP/1.1")
    }

    @Test
    fun matchEventDisabled(){
        val url = server.url("/match")
        val config = Config(Channel(URL(url.toString()), false), Channel(URL(url.toString()), false))

        val context = RuntimeEnvironment.application
        Velocidi.instance =  Velocidi(config, AdvertisingInfo("123", true), context)

        Velocidi.match("provider1", listOf(UserId("eml","mail@example.com")))
        val response = server.takeRequest(2, TimeUnit.SECONDS)
        assertThat(response).isNull()
    }

    @Test
    fun accumulateRequestWhileAaidUndefined(){
        Velocidi.match("provider1", listOf(UserId("eml","mail@example.com")))
        Velocidi.match("provider1", listOf(UserId("eml","mail@example.com")))
        Velocidi.match("provider1", listOf(UserId("eml","mail@example.com")))

        assertThat(Velocidi.queue.size).isEqualTo(3)
    }

    @Test
    fun trackingDisabled(){
        val url = server.url("/")
        val config = Config(Channel(URL(url.toString()), true), Channel(URL(url.toString()), true))

        val context = RuntimeEnvironment.application
        Velocidi.instance =  Velocidi(config, AdvertisingInfo("123", false), context)

        Velocidi.match("provider1", listOf(UserId("eml","mail@example.com")))
        Velocidi.track(PageView())
        val response = server.takeRequest(2, TimeUnit.SECONDS)
        assertThat(response).isNull()
    }
}

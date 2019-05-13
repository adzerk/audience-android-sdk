package com.velocidi

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import com.velocidi.events.PageView
import com.velocidi.util.VelocidiMockAsync
import com.velocidi.util.VelocidiMockSync
import com.velocidi.util.containsRequestLine
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
class VelocidiTest {

    var server = MockWebServer()

    @Rule
    @JvmField
    val globalTimeout = Timeout.seconds(10) // 10 seconds max per method tested

    @Test
    fun configSDK() {
        val config = Config(Uri.parse("http://example.com"))

        assertThat(config.track.host.toString()).isEqualTo("http://tr.example.com/events")
        assertThat(config.track.enabled).isTrue()
        assertThat(config.match.host.toString()).isEqualTo("http://match.example.com/match")
        assertThat(config.match.enabled).isTrue()
    }

    @Test
    fun trackingEvent() {
        val url = Uri.parse(server.url("/tr").toString())
        val config = Config(Channel(url, true), Channel(url, false))

        val context: Context = ApplicationProvider.getApplicationContext()

        Velocidi.instance = VelocidiMockSync(config, context)

        server.enqueue(MockResponse())

        Velocidi.getInstance().track(PageView("site1", "clientId1"))

        val response = server.takeRequest()
        response.containsRequestLine("GET /tr?siteId=site1&clientId=clientId1&type=pageView&cookies=false&id_gaid=123 HTTP/1.1")
    }

    @Test
    fun trackingEventDisabled() {
        val url = Uri.parse(server.url("/tr").toString())
        val config = Config(Channel(url, false), Channel(url, false))

        val context: Context = ApplicationProvider.getApplicationContext()

        Velocidi.instance = VelocidiMockSync(config, context)

        Velocidi.getInstance().track(PageView("site1", "clientId1"))

        val response = server.takeRequest(2, TimeUnit.SECONDS)
        assertThat(response).isNull()
    }

    @Test
    fun matchEvent() {
        val url = Uri.parse(server.url("/match").toString())
        val config = Config(Channel(url, false), Channel(url, true))

        val context: Context = ApplicationProvider.getApplicationContext()

        Velocidi.instance = VelocidiMockSync(config, context)

        Velocidi.getInstance().match("provider1", listOf(UserId("eml", "mail@example.com")))

        val response = server.takeRequest()
        response.containsRequestLine("GET /match?providerId=provider1&id_eml=mail%40example.com&cookies=false&id_gaid=123 HTTP/1.1")
    }

    @Test
    fun matchEventDisabled() {
        val url = Uri.parse(server.url("/match").toString())
        val config = Config(Channel(url, false), Channel(url, false))

        val context: Context = ApplicationProvider.getApplicationContext()
        Velocidi.instance = VelocidiMockSync(config, context)

        Velocidi.getInstance().match("provider1", listOf(UserId("eml", "mail@example.com")))

        val response = server.takeRequest(2, TimeUnit.SECONDS)
        assertThat(response).isNull()
    }

    @Test
    fun shouldNotTrackUser() {
        val url = Uri.parse(server.url("/").toString())
        val config = Config(Channel(url, true), Channel(url, true))

        val context: Context = ApplicationProvider.getApplicationContext()

        Velocidi.instance = VelocidiMockSync(config, context, AdvertisingInfo("123", false))

        Velocidi.getInstance().match("provider1", listOf(UserId("eml", "mail@example.com")))
        Velocidi.getInstance().track(PageView("site1", "clientId1"))

        val response = server.takeRequest(2, TimeUnit.SECONDS)
        assertThat(response).isNull()
    }

    @Test
    fun accumulateRequestWhileAdidUndefined() {

        val url = Uri.parse(server.url("/").toString())

        val config = Config(Channel(url, false), Channel(url, false))

        val context: Context = ApplicationProvider.getApplicationContext()
        val instance = VelocidiMockAsync(config, context)

        instance.match("provider1", listOf(UserId("eml", "mail@example.com")))
        instance.match("provider1", listOf(UserId("eml", "mail@example.com")))
        instance.match("provider1", listOf(UserId("eml", "mail@example.com")))

        assertThat(instance.queue.size).isEqualTo(3)

        // Force the onPostExecute method from the async task to be executed in the main thread
        Robolectric.flushForegroundThreadScheduler()
    }
}

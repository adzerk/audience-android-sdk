package com.velocidi

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.velocidi.events.PageView
import com.velocidi.util.containsRequestLine
import java.util.concurrent.TimeUnit
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class VelocidiTest {

    var server = MockWebServer()

    @Rule
    @JvmField
    val globalTimeout: Timeout = Timeout.seconds(10) // 10 seconds max per method tested

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

        Velocidi.instance = Velocidi(config, context)

        server.enqueue(MockResponse())

        Velocidi.getInstance().track(UserId("123"), PageView("site1", "clientId1"))

        val response = server.takeRequest()
        response.containsRequestLine(
            "GET /tr?cookies=false&siteId=site1&clientId=clientId1&type=pageView&id_gaid=123 HTTP/1.1"
        )
    }

    @Test
    fun trackingEventDisabled() {
        val url = Uri.parse(server.url("/tr").toString())
        val config = Config(Channel(url, false), Channel(url, false))

        val context: Context = ApplicationProvider.getApplicationContext()

        Velocidi.instance = Velocidi(config, context)

        Velocidi.getInstance().track(UserId("123"), PageView("site1", "clientId1"))

        val response = server.takeRequest(2, TimeUnit.SECONDS)
        assertThat(response).isNull()
    }

    @Test
    fun matchEvent() {
        val url = Uri.parse(server.url("/match").toString())
        val config = Config(Channel(url, false), Channel(url, true))

        val context: Context = ApplicationProvider.getApplicationContext()

        Velocidi.instance = Velocidi(config, context)

        Velocidi.getInstance().match(
            "provider1",
            listOf(
                UserId("123"),
                UserId(
                    "81df589b1dceacc2fa7c8f536015fcdf854eee721fdf282a91ed9c4b0c54dc76",
                    "email_sha256"
                )
            )
        )

        val response = server.takeRequest()
        response.containsRequestLine(
            "GET /match?cookies=false&id_gaid=123&id_email_sha256=81df589b1dceacc2fa7c8f536015fcdf854eee721fdf282a91ed9c4b0c54dc76&providerId=provider1 HTTP/1.1"
        )
    }

    @Test
    fun matchEventDisabled() {
        val url = Uri.parse(server.url("/match").toString())
        val config = Config(Channel(url, false), Channel(url, false))

        val context: Context = ApplicationProvider.getApplicationContext()
        Velocidi.instance = Velocidi(config, context)

        Velocidi.getInstance().match(
            "provider1",
            listOf(
                UserId("123"),
                UserId(
                    "81df589b1dceacc2fa7c8f536015fcdf854eee721fdf282a91ed9c4b0c54dc76",
                    "email_sha256"
                )
            )
        )

        val response = server.takeRequest(2, TimeUnit.SECONDS)
        assertThat(response).isNull()
    }

    @Test
    fun throwWhenMatchDoesntMeetRequirements() {
        val url = Uri.parse(server.url("/match").toString())
        val config = Config(Channel(url, false), Channel(url, true))

        val context: Context = ApplicationProvider.getApplicationContext()

        Velocidi.instance = Velocidi(config, context)

        // Empty providerId
        assertThatThrownBy {
            Velocidi.getInstance().match(
                "",
                listOf(
                    UserId("123"),
                    UserId(
                        "81df589b1dceacc2fa7c8f536015fcdf854eee721fdf282a91ed9c4b0c54dc76",
                        "email_sha256"
                    )
                )
            )
        }

        val response1 = server.takeRequest(2, TimeUnit.SECONDS)
        assertThat(response1).isNull()

        // userId length < 2
        assertThatThrownBy {
            Velocidi.getInstance().match(
                "provider1",
                listOf(
                    UserId("123")
                )
            )
        }

        val response2 = server.takeRequest(2, TimeUnit.SECONDS)
        assertThat(response2).isNull()
    }
}

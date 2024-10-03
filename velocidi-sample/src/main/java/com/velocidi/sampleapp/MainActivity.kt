package com.velocidi.sampleapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.velocidi.UserId
import com.velocidi.Velocidi
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
        val trackButton = findViewById<Button>(R.id.track_button)
        val matchButton = findViewById<Button>(R.id.match_button)

        trackButton.setOnClickListener {
            val eventJsonString =
                """
                {
                  "clientId": "velocidi",
                  "siteId": "velocidi.com",
                  "type": "appView",
                  "customFields": {
                    "debug": true,
                    "role": "superuser"
                  },
                  "title": "Welcome Screen"
                }
                """.trimIndent()

            Velocidi.getInstance().track(
                // test@example.org
                UserId(
                    "388c735eec8225c4ad7a507944dd0a975296baea383198aa87177f29af2c6f69",
                    "email_sha256",
                ),
                eventJsonString,
            )

            val eventJsonObj =
                mapOf(
                    "clientId" to "velocidi",
                    "siteId" to "velocidi.com",
                    "type" to "appView",
                    "title" to "Welcome Screen",
                    "customFields" to
                        mapOf(
                            "debug" to true,
                            "role" to "superuser",
                        ),
                )

            Velocidi.getInstance().track(
                // test@example.org
                UserId(
                    "388c735eec8225c4ad7a507944dd0a975296baea383198aa87177f29af2c6f69",
                    "email_sha256",
                ),
                JSONObject(eventJsonObj),
            )
        }

        matchButton.setOnClickListener {
            Velocidi.getInstance().match(
                "web",
                listOf(
                    // test@example.org
                    UserId(
                        "388c735eec8225c4ad7a507944dd0a975296baea383198aa87177f29af2c6f69",
                        "email_sha256",
                    ),
                    UserId("user_advertising_id", "gaid"),
                ),
            )
        }
    }
}

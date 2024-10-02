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
                UserId("user_email_hash", "email_sha256"),
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
                UserId("user_email_hash", "email_sha256"),
                JSONObject(eventJsonObj),
            )
        }

        matchButton.setOnClickListener {
            Velocidi.getInstance().match(
                "someProvider",
                listOf(
                    UserId("user_email_hash", "email_sha256"),
                    UserId("user_advertising_id", "gaid"),
                ),
            )
        }
    }
}

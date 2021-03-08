package com.velocidi.sampleapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.velocidi.UserId
import com.velocidi.Velocidi
import com.velocidi.events.PageView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
        val trackButton = findViewById<Button>(R.id.track_button)
        val matchButton = findViewById<Button>(R.id.match_button)

        trackButton.setOnClickListener {
//            val event =
//                """
//                {
//                  "type": "pageView",
//                  "siteId": "MobileApp",
//                  "clientId": "client1"
//                }
//                """.trimIndent()

            Velocidi.getInstance().track(UserId("test"), PageView("MobileApp", "client1"))

            // OR
            // Velocidi.getInstance().track(CustomTrackingEventFactory.buildFromJSON(event))
        }

        matchButton.setOnClickListener {
            Velocidi.getInstance().match(
                "someProvider",
                listOf(UserId("eml", "useremail@example.com"))
            )
        }
    }
}

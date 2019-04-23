package com.velocidi.sampleapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.velocidi.UserId
import com.velocidi.Velocidi

import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        track_button.setOnClickListener {
            val event =
                """
                {
                  "type": "pageView",
                  "siteId": "MobileApp",
                  "clientId": "client1"
                }
                """.trimIndent()
            Velocidi.getInstance().track(JSONObject(event))
        }

        match_button.setOnClickListener {
            Velocidi.getInstance().match("someProvider", listOf(UserId("eml", "useremail@example.com")))
        }
    }
}

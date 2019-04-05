package com.velocidi.sampleapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import com.velocidi.Config
import com.velocidi.UserId
import com.velocidi.Velocidi

import kotlinx.android.synthetic.main.activity_main2.*
import org.json.JSONObject
import java.net.URL


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(toolbar)

        track_button.setOnClickListener {
            val event = """
                {
                  "eventType": "productClick",
                  "siteId": "1",
                  "clientId": "client1"
                }
            """.trimIndent()
            Velocidi.track(JSONObject(event))
        }

        match_button.setOnClickListener {
            Velocidi.match("someProvider", listOf(UserId("eml", "useremail@example.com")))
        }

        val config = Config(URL("http://test.com"))

        Velocidi.start(config, this)
    }

}

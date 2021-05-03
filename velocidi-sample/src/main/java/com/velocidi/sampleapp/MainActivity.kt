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
            val eventJsonObj = mapOf(
                "foo" to "exampleA",
                "bar" to 42,
                "baz" to true,
                "quuxFoo" to arrayOf(
                    mapOf(
                        "a" to "1",
                        "b" to 2,
                        "c" to mapOf(
                            "a" to arrayOf(
                                1,
                                2,
                                3
                            ),
                            "b" to "bGrault",
                            "c" to arrayOf<Any>(),
                            "d" to mapOf<String, Any>()
                        ),
                        "d" to true
                    )
                ),
                "corge" to false
            )

            Velocidi.getInstance().track(
                UserId("user_email_hash", "email_sha256"),
                JSONObject(eventJsonObj)
            )
        }

        matchButton.setOnClickListener {
            Velocidi.getInstance().match(
                "someProvider",
                listOf(
                    UserId("user_email_hash", "email_sha256"),
                    UserId("user_advertising_id", "gaid")
                )
            )
        }
    }
}

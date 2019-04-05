package com.velocidi.sampleapp

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
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

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            Velocidi.track(JSONObject())
        }

        val callFuncBegin = System.currentTimeMillis()

        val config = Config(URL("http://test.com"))

        Velocidi.start(config, this)
        Velocidi.track(JSONObject())
        Velocidi.track(JSONObject())
        Velocidi.track(JSONObject())
        Velocidi.match("p1", listOf(UserId("eml", "123")))
        Velocidi.match("p1", listOf(UserId("eml", "123")))

        val callFuncEnd = System.currentTimeMillis()

        val callFuncDiff = Math.abs(callFuncEnd - callFuncBegin)

        Log.e("TEST", callFuncDiff.toString())

    }


}

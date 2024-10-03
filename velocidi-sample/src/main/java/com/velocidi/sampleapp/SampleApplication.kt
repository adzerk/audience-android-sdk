package com.velocidi.sampleapp

import android.app.Application
import android.net.Uri
import com.velocidi.Channel
import com.velocidi.Config
import com.velocidi.Velocidi

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Example: https://tr.cdp.example.audience.kevel.com/events
        val trackEndpoint = Channel(Uri.parse("http://localhost"), true)
        // Example: https://match.cdp.example.audience.kevel.com/events
        val matchEndpoint = Channel(Uri.parse("http://localhost"), true)
        val config = Config(trackEndpoint, matchEndpoint)

        // OR
        // val config = Config(URL("https://test.com"))

        Velocidi.init(config, this)
    }
}

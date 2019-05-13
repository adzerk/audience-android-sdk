package com.velocidi.sampleapp

import android.app.Application
import android.net.Uri
import com.velocidi.Channel
import com.velocidi.Config
import com.velocidi.Velocidi

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val trackEndpoint = Channel(Uri.parse("http://tr.cdp.velocidi.com/events"), true)
        val matchEndpoint = Channel(Uri.parse("http://match.cdp.velocidi.com/match"), true)
        val config = Config(trackEndpoint, matchEndpoint)

        // OR
        // val config = Config(URL("https://test.com"))

        Velocidi.init(config, this)
    }
}

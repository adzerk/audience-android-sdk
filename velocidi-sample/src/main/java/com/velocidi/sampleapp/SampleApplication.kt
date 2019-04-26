package com.velocidi.sampleapp

import android.app.Application
import com.velocidi.Channel
import com.velocidi.Config
import com.velocidi.Velocidi
import java.net.URL

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val trackEndpoint = Channel(URL("https://tr.test.com/events"), true)
        val matchEndpoint = Channel(URL("https://match.test.com/match"), true)
        val config = Config(trackEndpoint, matchEndpoint)

        // OR
        // val config = Config(URL("https://test.com"))

        Velocidi.init(config, this)
    }
}

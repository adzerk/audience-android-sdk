package com.velocidi.sampleapp

import android.app.Application
import com.velocidi.Channel
import com.velocidi.Config
import com.velocidi.Velocidi
import java.net.URL

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val trackEndpoint = Channel(URL("http://tr.test.com"), true)
        val matchEndpoint = Channel(URL("http://match.test.com"), true)
        val config = Config(trackEndpoint, matchEndpoint)

        // OR
        // val config = Config(URL("http://test.com"))

        Velocidi.start(config, this)
    }
}

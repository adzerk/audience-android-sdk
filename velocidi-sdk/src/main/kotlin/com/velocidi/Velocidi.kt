package com.velocidi

import android.util.Log
import com.android.volley.Request
import org.json.JSONObject

class Velocidi private constructor(val config: Config) {

    private val client = HttpClient()

    private fun track(attributes: JSONObject) {
        if(!config.track.enabled) return

        client.sendRequest(Request.Method.POST, config.track.host.toString(), attributes)
    }

    private fun match(providerId: String, userIds: List<UserId>) {
        if(!config.match.enabled) return

        val params = mutableListOf<String>()
        params.add("providerId=$providerId")
        userIds.map { it.toQueryString() }.forEach { params.add(it) }

        val queryParams = params.joinToString("&")
        val url = "${config.match.host}?$queryParams"

        client.sendRequest(Request.Method.GET, url)
    }

     companion object {
         private lateinit var instance: Velocidi

         fun start(config: Config) {
             instance = Velocidi(config)
         }

         fun getInstance(): Velocidi? =
             when(::instance.isInitialized){
                 true -> instance
                 false -> {Log.e("Velocidi SDK", "Velocidi must be initialized"); null}
             }

         fun track(attributes: JSONObject) =
            getInstance()?.track(attributes)

         fun match(providerId: String, userIds: List<UserId>) =
             getInstance()?.match(providerId, userIds)
         }
}
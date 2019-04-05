package com.velocidi

import android.Manifest
import android.content.Context
import android.util.Log
import com.android.volley.Request
import org.json.JSONObject
import java.util.*


class Velocidi constructor(val config: Config, val adInfo: AdvertisingInfo, context: Context) {

    private val client = HttpClient()

    private val appInfo = Util.getApplicationInfo(context)

    init {
        client.headers["User-Agent"] = "${appInfo.appName}/${appInfo.appVersion} ${appInfo.androidSDK} ${appInfo.device}"
        client.defaultParams["aaid"] = adInfo.id
        client.defaultParams["cookies"] = "false"
    }

     private fun handleRequest(req: com.velocidi.Request) {
        if(!adInfo.shouldTrack) return

         return when(req) {
            is com.velocidi.Request.TrackRequest ->
                if(config.track.enabled)
                    client.sendRequest(Request.Method.POST, config.track.host.toString(), req.attributes)
                else return
            is com.velocidi.Request.MatchRequest ->
                if(config.match.enabled)
                    client.sendRequest(Request.Method.GET, "${config.match.host}?${req.toQueryParams()}")
                else return

        }
    }

     companion object {
         internal lateinit var instance: Velocidi

         internal val queue: Queue<com.velocidi.Request> = FixedSizeQueue(300)

         fun start(config: Config, context: Context) {
             if(!Util.checkPermission(context, Manifest.permission.INTERNET))
                 Log.i("Velocidi - SDK", "Velocidi SDK requires Internet permission")

             val listener = object : AdvertisingIdListener {
                 override fun fetchAdvertisingIdCompleted(advertisingInfo: AdvertisingInfo) {
                     instance = Velocidi(config, advertisingInfo, context)
                     while(queue.size != 0){
                         getInstance()?.handleRequest(queue.remove())
                     }
                 }
             }
             GetAdvertisingIdTask(listener).execute(context)

         }

         fun getInstance(): Velocidi? =
             when(::instance.isInitialized){
                 true -> instance
                 false -> {Log.e("Velocidi SDK", "Velocidi must be initialized"); null}
             }

         fun track(attributes: JSONObject) {
             val request = com.velocidi.Request.TrackRequest(attributes)
             getInstance()?.handleRequest(request) ?: queue.add(request)
         }

         fun match(providerId: String, userIds: List<UserId>) {
             val request = com.velocidi.Request.MatchRequest(providerId, userIds)
             getInstance()?.handleRequest(request) ?: queue.add(request)

             Log.e("Velocidi - SDK", "queue ${queue.size}")
         }
     }
}

sealed class Request{
    data class TrackRequest(val attributes: JSONObject) : com.velocidi.Request()
    data class MatchRequest(val providerId: String, val userIds: List<UserId>) : com.velocidi.Request(){
        fun toQueryParams(): String {
            val params = mutableListOf<String>()
            params.add("providerId=$providerId")
            userIds.map { it.toQueryString() }.forEach { params.add(it) }
            return params.joinToString("&")
        }
    }
}

package com.velocidi

<<<<<<< HEAD
import android.Manifest
import android.content.Context
import android.util.Log
import org.json.JSONObject


internal open class Velocidi constructor(val config: Config, context: Context): FetchAdvertisingId<Request>(context) {

    val client = HttpClient()

    val appInfo = Util.getApplicationInfo(context)

    init {
        client.headers["User-Agent"] = "${appInfo.appName}/${appInfo.appVersion} ${appInfo.androidSDK} ${appInfo.device}"
        client.defaultParams["cookies"] = "false"
    }

    override fun onFetchCompleted() {
        client.defaultParams["aaid"] = adInfo.id
    }

     override fun handleTask(task: Request) {
        if(!adInfo.shouldTrack) return

         return when(task) {
            is com.velocidi.Request.TrackRequest ->
                if(config.track.enabled)
                    client.sendRequest(HttpClient.Verb.POST, config.track.host.toString(), task.attributes)
                else return
            is com.velocidi.Request.MatchRequest ->
                if(config.match.enabled)
                    client.sendRequest(HttpClient.Verb.GET, "${config.match.host}?${task.toQueryParams()}")
                else return

        }
    }

     companion object {
         internal lateinit var instance: Velocidi

         fun start(config: Config, context: Context) {
             if(!Util.checkPermission(context, Manifest.permission.INTERNET))
                 Log.e(Constants.LOG_TAG, "Velocidi SDK requires Internet permission")

             instance = Velocidi(config, context)

         }

         fun getInstance(): Velocidi? =
             when(::instance.isInitialized){
                 true -> instance
                 false ->throw IllegalStateException(
                     """Velocidi SDK is not initialized
                            Make sure to call Velocidi.start first.""")
             }

         fun track(attributes: JSONObject) {
             val request = com.velocidi.Request.TrackRequest(attributes)
             getInstance()?.runTask(request)
         }

         fun match(providerId: String, userIds: List<UserId>) {
             val request = com.velocidi.Request.MatchRequest(providerId, userIds)
             getInstance()?.runTask(request)
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

package com.velocidi

import android.Manifest
import android.content.Context
import android.util.Log
import com.velocidi.Util.appendToUrl
import org.json.JSONObject

open class Velocidi constructor(val config: Config, context: Context) : FetchAdvertisingId<Request>(context) {

    val client = HttpClient()

    val appInfo = Util.getApplicationInfo(context)

    override fun onFetchCompleted() {
        // client.defaultParams["aaid"] = adInfo.id
    }

    override fun handleTask(task: Request) {
        if (!adInfo.shouldTrack) return

        val headers = mapOf("User-Agent" to "${appInfo.appName}/${appInfo.appVersion} ${appInfo.androidSDK} ${appInfo.device}")

        val params = mapOf("cookies" to "false", "id_aaid" to adInfo.id)

        return when (task) {
            is Request.TrackRequest ->
                if (config.track.enabled)
                    client.sendRequest(HttpClient.Verb.POST, config.track.host, task.attributes, params, headers)
                else return
            is Request.MatchRequest ->
                if (config.match.enabled) {
                    val urlWithParams = appendToUrl(config.match.host, task.toQueryParams())
                    client.sendRequest(HttpClient.Verb.GET, urlWithParams, parameters = params, headers = headers)
                } else return
        }
    }

    fun track(attributes: JSONObject) {
        val request = Request.TrackRequest(attributes)
        runTask(request)
    }

    fun match(providerId: String, userIds: List<UserId>) {
        val request = Request.MatchRequest(providerId, userIds)
        runTask(request)
    }

    companion object {
        internal lateinit var instance: Velocidi

        fun init(config: Config, context: Context): Velocidi {
            if (!Util.checkPermission(context, Manifest.permission.INTERNET))
                Log.e(Constants.LOG_TAG, "Velocidi SDK requires Internet permission")

            instance = Velocidi(config, context)

            return instance
        }

        fun getInstance(): Velocidi =
            when (::instance.isInitialized) {
                true -> instance
                false -> throw IllegalStateException(
                    """Velocidi SDK is not initialized
                            Make sure to call Velocidi.init first."""
                )
            }
    }
}

sealed class Request {
    data class TrackRequest(val attributes: JSONObject) : Request()
    data class MatchRequest(val providerId: String, val userIds: List<UserId>) : Request() {
        fun toQueryParams(): Map<String, String> {
            val params = mutableMapOf<String, String>()
            params["providerId"] = providerId
            userIds.forEach { params["id_${it.type}"] = it.id }
            return params
        }
    }
}

package com.velocidi

import android.Manifest
import android.content.Context
import com.velocidi.Util.appendToUrl
import org.json.JSONObject
import java.util.*

/**
 * Class with the main Velocidi SDK logic
 *
 * @property config Configuration object
 *
 * @param context Android application context
 */
open class Velocidi constructor(val config: Config, context: Context) {

    val client = HttpClient()

    val appInfo = Util.getApplicationInfo(context)

    val queue: Queue<Request> = FixedSizeQueue(300)

    lateinit var adInfo: AdvertisingInfo

    init {
        // Start fetching the Advertising Id when Velocidi is instantiated
        fetchAndSetAdvertisingId(context)
    }

    /**
     * Initialize the retrieval of the Advertising Id
     * When it finished, flushes the queue of pending request
     *
     * @param context Android application context
     */
    open fun fetchAndSetAdvertisingId(context: Context) {
        GetAdvertisingIdTask { advertisingInfo ->
            this@Velocidi.adInfo = advertisingInfo

            while (queue.size != 0) {
                handleTask(queue.remove())
            }
        }.execute(context)
    }

    /**
     * Tries to execute a request. If the Advertising Id is still pending then the request is added to a queue
     * of pending requests
     *
     * @param req Task to be executed when the Advertising Id is set
     */
    fun runTask(req: Request) {
        if (::adInfo.isInitialized) handleTask(req) else queue.add(req)
    }

    /**
     * Executes a request
     * Currently, only supports Track and Match requests
     *
     * @param req Request to be process
     */
    fun handleTask(req: Request) {
        if (!adInfo.shouldTrack) return

        val headers =
            mapOf("User-Agent" to "${appInfo.appName}/${appInfo.appVersion} ${appInfo.androidSDK} ${appInfo.device}")

        val params = mapOf("cookies" to "false", "id_gaid" to adInfo.id)

        return when (req) {
            is Request.TrackRequest ->
                if (config.track.enabled)
                    client.sendRequest(HttpClient.Verb.POST, config.track.host, req.attributes, params, headers)
                else return
            is Request.MatchRequest ->
                if (config.match.enabled) {
                    val urlWithParams = config.match.host.appendToUrl(req.toQueryParams())
                    client.sendRequest(HttpClient.Verb.GET, urlWithParams, parameters = params, headers = headers)
                } else return
        }
    }

    /**
     * Collects activity performed by the user in the Android application
     *
     * @param attributes json object with event information
     */
    fun track(attributes: JSONObject) {
        val request = Request.TrackRequest(attributes)
        runTask(request)
    }

    /**
     * Match Google Advertising Id with the list of provided user ids
     *
     * @param providerId Id of the match provider
     * @param userIds List of user ids to be linked along with Advertising Id
     */
    fun match(providerId: String, userIds: List<UserId>) {
        val request = Request.MatchRequest(providerId, userIds)
        runTask(request)
    }

    companion object {
        // Singleton instance of the sdk
        internal lateinit var instance: Velocidi

        /**
         * Initiates Velocidi SDK. This method must be called before making any track or match request
         *
         * @throws SecurityException Throws an exception if the Android application doesn't
         * have the necessary permissions
         *
         * @param config Configuration object
         * @param context Android application context
         * @return Velocidi instance
         */
        fun init(config: Config, context: Context): Velocidi {
            if (!Util.checkPermission(context, Manifest.permission.INTERNET))
                throw SecurityException("Velocidi SDK requires Internet permission")

            instance = Velocidi(config, context)

            return instance
        }

        /**
         * Getter for the Velocidi instance
         *
         * @throws IllegalStateException Throws if the SDK wasn't initialized first
         *
         * @return The Velocidi instance
         */
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

// ADT with the supported requests
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

package com.velocidi

import android.Manifest
import android.content.Context
import com.velocidi.events.*
import com.velocidi.Util.appendToUrl
import java.util.Queue

/**
 * Class with the main Velocidi SDK logic.
 *
 * An instance of `Velocidi` allows the client to call `match` and `track` methods to send events
 * to the Adality CDP servers.
 *
 * Before using any instance of `Velocidi` one should call `init` in the companion object in order
 * to configure the server endpoints. After this, one can call `getInstance` at any time to fetch
 * the instance of `Velocidi` which is now ready to use.
 *
 * @property config Configuration object
 *
 * @param context Android application context
 */
open class Velocidi constructor(val config: Config, context: Context) {

    private val client = HttpClient()

    private val appInfo = Util.getApplicationInfo(context)

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
    protected open fun fetchAndSetAdvertisingId(context: Context) {
        GetAdvertisingIdTask { advertisingInfo ->
            this@Velocidi.adInfo = advertisingInfo
            emptyTaskQueue()
        }.execute(context)
    }

    protected fun emptyTaskQueue() {
        while (queue.size != 0) {
            handleTask(queue.remove())
        }
    }

    /**
     * Tries to execute a request. If the Advertising Id is still pending then the request is added to a queue
     * of pending requests
     *
     * @param req Task to be executed when the Advertising Id is set
     */
    private fun runTask(req: Request) {
        if (::adInfo.isInitialized) handleTask(req) else queue.add(req)
    }

    /**
     * Executes a request
     * Currently, only supports Track and Match requests
     *
     * @param req Request to be process
     */
    private fun handleTask(req: Request) {
        if (!adInfo.shouldTrack) return

        val headers =
            mapOf(
                "User-Agent" to Util.buildUserAgent(appInfo)
            )

        val params = mapOf("cookies" to "false", "id_gaid" to adInfo.id)

        return when (req) {
            is Request.TrackRequest ->
                if (config.track.enabled) {
                    val urlWithParams =
                        config.track.host.appendToUrl(req.attributes.toQueryParams())
                    client.sendRequest(
                        HttpClient.Verb.GET,
                        urlWithParams,
                        parameters = params,
                        headers = headers
                    )
                } else return
            is Request.MatchRequest ->
                if (config.match.enabled) {
                    val urlWithParams = config.match.host.appendToUrl(req.toQueryParams())

                    client.sendRequest(
                        HttpClient.Verb.GET,
                        urlWithParams,
                        parameters = params,
                        headers = headers
                    )
                } else return
        }
    }

    /**
     * Collects activity performed by the user in the Android application
     *
     * @param attributes json object with event information
     */
    fun track(event: TrackingEvent) {
        val request = Request.TrackRequest(event)
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
    data class TrackRequest(val attributes: TrackingEvent) : Request()
    data class MatchRequest(val providerId: String, val userIds: List<UserId>) : Request() {
        fun toQueryParams(): Map<String, String> {
            val params = mutableMapOf<String, String>()
            params["providerId"] = providerId
            userIds.forEach { params["id_${it.type}"] = it.id }
            return params
        }
    }
}

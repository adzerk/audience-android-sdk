package com.velocidi

import android.Manifest
import android.content.Context
import com.velocidi.Util.toQueryParams
import org.json.JSONObject

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
open class Velocidi internal constructor(
    val config: Config,
    context: Context,
) {
    private val client = HttpClient()

    private val appInfo = Util.getApplicationInfo(context)

    /**
     * Executes a request
     * Currently, only supports Track and Match requests
     *
     * @param req Request to be process
     */
    private fun handleTask(req: Request) {
        val headers =
            mapOf(
                "User-Agent" to Util.buildUserAgent(appInfo),
            )

        val commonParams = mapOf("cookies" to "false")

        return when (req) {
            is Request.TrackRequest ->
                if (config.track.enabled) {
                    val entity = commonParams + req.event.toQueryParams() + req.userId.toPair()
                    client.sendRequest(
                        HttpClient.Verb.GET,
                        config.track.host,
                        parameters = entity,
                        headers = headers,
                    )
                } else {
                    return
                }
            is Request.MatchRequest ->
                if (config.match.enabled) {
                    val entity = commonParams + req.toQueryParams()

                    client.sendRequest(
                        HttpClient.Verb.GET,
                        config.match.host,
                        parameters = entity,
                        headers = headers,
                    )
                } else {
                    return
                }
        }
    }

    /**
     * Collects activity performed by the user in the Android application
     *
     * For more information, about event collection and supported event,
     * see https://docs.velocidi.com/collect/methods and https://docs.velocidi.com/collect/events
     *
     * @param userId User identifier
     * @param event Json String with the event performed by the user
     * @throws JSONException if the parsing the event fails
     */
    fun track(
        userId: UserId,
        event: String,
    ) {
        val obj = JSONObject(event)

        val request = Request.TrackRequest(userId, obj)
        handleTask(request)
    }

    /**
     * Collects activity performed by the user in the Android application
     *
     * For more information, about event collection and supported event,
     * see https://docs.velocidi.com/collect/methods and https://docs.velocidi.com/collect/events
     *
     * @param userId User identifier
     * @param event JSONObject with the event performed by the user
     */
    fun track(
        userId: UserId,
        event: JSONObject,
    ) {
        val request = Request.TrackRequest(userId, event)
        handleTask(request)
    }

    /**
     * Identify a user across multiple channels
     *
     * For more information, see https://docs.velocidi.com/collect/matches
     *
     * @param providerId Id of the match provider
     * @param userIds List of user ids to be linked
     * @throws IllegalArgumentException Throws when providerId is empty or userIds size is smaller than 2
     */
    fun match(
        providerId: String,
        userIds: List<UserId>,
    ) {
        require(providerId.isNotEmpty()) { "providerId cannot be empty" }
        require(userIds.size >= 2) { "must provide at least 2 userIds" }

        val request = Request.MatchRequest(providerId, userIds)
        handleTask(request)
    }

    companion object {
        // Singleton instance of the sdk
        @JvmSynthetic
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
        @JvmStatic
        fun init(
            config: Config,
            context: Context,
        ): Velocidi {
            if (!Util.checkPermission(context, Manifest.permission.INTERNET)) {
                throw SecurityException("Velocidi SDK requires Internet permission")
            }

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
        @JvmStatic
        fun getInstance(): Velocidi =
            when (::instance.isInitialized) {
                true -> instance
                false -> throw IllegalStateException(
                    """Velocidi SDK is not initialized
                            Make sure to call Velocidi.init first.""",
                )
            }
    }
}

// ADT with the supported requests
internal sealed class Request {
    data class TrackRequest(
        val userId: UserId,
        val event: JSONObject,
    ) : Request()

    data class MatchRequest(
        val providerId: String,
        val userIds: List<UserId>,
    ) : Request() {
        fun toQueryParams(): Map<String, String> {
            val userIdsMap = userIds.map { it.toPair() }.toMap()
            return userIdsMap + Pair("providerId", providerId)
        }
    }
}

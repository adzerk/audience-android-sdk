package com.velocidi

import android.Manifest
import android.content.Context
import com.velocidi.events.*

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
open class Velocidi internal constructor(val config: Config, context: Context) {

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
                "User-Agent" to Util.buildUserAgent(appInfo)
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
                        headers = headers
                    )
                } else return
            is Request.MatchRequest ->
                if (config.match.enabled) {
                    val entity = commonParams + req.toQueryParams()

                    client.sendRequest(
                        HttpClient.Verb.GET,
                        config.match.host,
                        parameters = entity,
                        headers = headers
                    )
                } else return
        }
    }

    /**
     * Collects activity performed by the user in the Android application
     *
     * For more information, see https://docs.velocidi.com/collect/methods
     *
     * @param userId User identifier
     * @param event Event performed by the user
     */
    fun track(userId: UserId, event: TrackingEvent) {
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
     */
    fun match(providerId: String, userIds: List<UserId>) {
        val request = Request.MatchRequest(providerId, userIds)
        handleTask(request)
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
internal sealed class Request {
    data class TrackRequest(val userId: UserId, val event: TrackingEvent) : Request()
    data class MatchRequest(val providerId: String, val userIds: List<UserId>) : Request() {
        fun toQueryParams(): Map<String, String> {
            val userIdsMap = userIds.map { it.toPair() }.toMap()
            return userIdsMap + Pair("providerId", providerId)
        }
    }
}

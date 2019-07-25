package com.velocidi.events

/**
 * Search event
 *
 * @property siteId Client's site identifier
 * @property clientId Client identifier
 */
data class Search(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("search") {

    /**
     * Query searched by the user
     */
    var query: String? = null
}

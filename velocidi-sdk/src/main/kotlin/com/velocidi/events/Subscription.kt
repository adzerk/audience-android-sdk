package com.velocidi.events

/**
 * Subscription event
 *
 * @property siteId Client's site identifier
 * @property clientId Client identifier
 */
data class Subscription(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("subscription") {

    /**
     * Products associated with the Subscription
     */
    var products: List<Product> = emptyList()

    /**
     * Transaction associated with the Subscription
     */
    var transaction: Transaction? = null
}

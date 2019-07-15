package com.velocidi.events

/**
 * Purchase event
 *
 * @property siteId Client's site identifier
 * @property clientId Client identifier
 */
data class Purchase(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("purchase") {

    /**
     * Products purchased
     */
    var products: List<Product> = emptyList()

    /**
     * Transaction associated with the Purchase
     */
    var transaction: Transaction? = null
}

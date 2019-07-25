package com.velocidi.events

/**
 * Remove from cart event
 *
 * @property siteId Client's site identifier
 * @property clientId Client identifier
 */
data class RemoveFromCart(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("removeFromCart") {

    /**
     * Products removed from the cart
     */
    var products: List<Product> = emptyList()
}

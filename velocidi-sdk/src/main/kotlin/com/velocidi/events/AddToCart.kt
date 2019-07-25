package com.velocidi.events

/**
 * Add to cart event
 *
 * @property siteId Client's site identifier
 * @property clientId Client identifier
 */
class AddToCart(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("addToCart") {

    /**
     * Products added to the cart
     */
    var products: List<Product> = emptyList()
}

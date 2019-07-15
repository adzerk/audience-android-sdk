package com.velocidi.events

/**
 * Product click event
 *
 * @property siteId Client's site identifier
 * @property clientId Client identifier
 */
data class ProductClick(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("productClick") {

    /**
     * Clicked products
     */
    var products: List<Product> = emptyList()
}

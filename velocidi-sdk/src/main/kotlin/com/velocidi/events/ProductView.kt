package com.velocidi.events

/**
 * Product view event
 *
 * @property siteId Client's site identifier
 * @property clientId Client identifier
 */
data class ProductView(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("productView") {

    /**
     * List of products viewed
     */
    var products: List<Product> = emptyList()
}

package com.velocidi.events

/**
 * Product impression event
 *
 * @property siteId Client's site identifier
 * @property clientId Client identifier
 */
data class ProductImpression(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("productImpression") {

    /**
     * List of products associated with this Product Impression
     */
    var products: List<Product>? = emptyList()
}

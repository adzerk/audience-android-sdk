package com.velocidi.events

/**
 * Product view details event
 *
 * @property siteId Client's site identifier
 * @property clientId Client identifier
 */
data class ProductViewDetails(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("productViewDetails") {

    /**
     * List of products whose details have been viewed
     */
    var products: List<Product> = emptyList()
}

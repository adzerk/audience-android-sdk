package com.velocidi.events

/**
 * Product feedback event
 *
 * @property siteId Client's site identifier
 * @property clientId Client identifier
 */
data class ProductFeedback(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("productFeedback") {

    /**
     * List of products associated with this product feedback
     */
    var products: List<Product> = emptyList()

    /**
     * Rating given by the user to the product
     */
    var rating: Double? = null

    /**
     * Text feedback given by the user to the product
     */
    var feedback: String? = null
}

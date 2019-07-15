package com.velocidi.events

/**
 * Refund event
 *
 * @property siteId Client's site identifier
 * @property clientId Client identifier
 * @property refundType Type of refund (partial or total)
 */
data class Refund(
    override val siteId: String,
    override val clientId: String,
    val refundType: String
) : TrackingEvent("refund") {

    /**
     * If it is a `partial` refund, the list of products to be refunded must be specified
     */
    var products: List<Product>? = emptyList()

    /**
     * Transaction associated with this refund
     */
    var transaction: Transaction? = null
}

package com.velocidi.events

data class OrderPlace(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("orderPlace") {
    /**
     * Line items ordered
     */
    var lineItems: List<LineItem> = emptyList()

    /**
     * Order details
     */
    var order: Order? = null
}

package com.velocidi.events

data class ProductFeedback(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("productFeedback") {
    var products: List<Product> = emptyList()
    var rating: Double? = null
    var feedback: String? = null
}

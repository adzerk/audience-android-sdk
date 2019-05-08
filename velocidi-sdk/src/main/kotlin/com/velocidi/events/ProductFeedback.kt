package com.velocidi.events

data class ProductFeedback(
    override val siteId: String,
    override val clientId: String,
    val products: List<Product> = emptyList(),
    val rating: Double? = null,
    val feedback: String? = null
) : TrackingEvent("productFeedback") {
    override fun serialize(): String =
        defaultGson().toJson(this)
}

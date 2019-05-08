package com.velocidi.events

data class ProductImpression(
    override val siteId: String,
    override val clientId: String,
    val products: List<Product>? = emptyList()
) : TrackingEvent("productImpression") {
    override fun serialize(): String =
        defaultGson().toJson(this)
}

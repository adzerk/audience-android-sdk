package com.velocidi.events

data class ProductViewDetails(
    override val siteId: String,
    override val clientId: String,
    val products: List<Product> = emptyList()
) : TrackingEvent("productViewDetails") {
    override fun serialize(): String =
        defaultGson().toJson(this)
}

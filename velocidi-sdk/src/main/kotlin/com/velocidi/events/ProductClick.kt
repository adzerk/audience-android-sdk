package com.velocidi.events

data class ProductClick(
    override val siteId: String,
    override val clientId: String,
    val products: List<Product> = emptyList()
) : TrackingEvent("productClick") {
    override fun serialize(): String =
        defaultGson().toJson(this)
}

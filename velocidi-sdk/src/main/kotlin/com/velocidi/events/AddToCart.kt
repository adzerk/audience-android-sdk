package com.velocidi.events

class AddToCart(
    override val siteId: String,
    override val clientId: String,
    val products: List<Product> = emptyList()
) : TrackingEvent("addToCart") {
    override fun serialize(): String =
        defaultGson().toJson(this)
}

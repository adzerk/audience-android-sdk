package com.velocidi.events

data class RemoveFromCart(
    override val siteId: String,
    override val clientId: String,
    val products: List<Product> = emptyList()
) : TrackingEvent("removeFromCart") {
    override fun serialize(): String =
        defaultGson().toJson(this)
}

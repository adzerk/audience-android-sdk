package com.velocidi.events

data class ProductView(
    override val siteId: String,
    override val clientId: String,
    val products: List<Product> = emptyList()
) : TrackingEvent("productView") {
    override fun serialize(): String =
        defaultGson().toJson(this)
}

package com.velocidi.events

data class Subscription(
    override val siteId: String,
    override val clientId: String,
    val products: List<Product> = emptyList(),
    val transaction: Transaction? = null
) : TrackingEvent("subscription") {
    override fun serialize(): String =
        defaultGson().toJson(this)
}

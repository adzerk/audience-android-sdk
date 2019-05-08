package com.velocidi.events

data class Purchase(
    override val siteId: String,
    override val clientId: String,
    val products: List<Product> = emptyList(),
    val transaction: Transaction? = null
) : TrackingEvent("purchase") {
    override fun serialize(): String =
        defaultGson().toJson(this)
}

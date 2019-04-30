package com.velocidi.events

import kotlinx.serialization.Serializable

@Serializable
data class Purchase(
    override val siteId: String,
    override val clientId: String,
    val products: List<Product> = emptyList(),
    val transaction: Transaction? = null
) : TrackingEvent("purchase") {
    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this)
}

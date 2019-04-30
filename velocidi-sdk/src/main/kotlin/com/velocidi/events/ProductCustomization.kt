package com.velocidi.events

import kotlinx.serialization.Serializable

@Serializable
data class ProductCustomization(
    override val siteId: String,
    override val clientId: String? = null,
    val product: List<Product> = emptyList(),
    val productCustomization: Customization? = null
) : TrackingEvent("productCustomization", siteId, clientId) {
    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this)

    companion object Properties {
        @Serializable
        data class Customization(
            val name: String,
            val value: String? = null,
            val price: Double? = null,
            val currency: String? = null
        )
    }
}

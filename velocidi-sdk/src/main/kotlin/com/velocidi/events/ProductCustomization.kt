package com.velocidi.events

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class ProductCustomization(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val product: Product? = null,
    @Optional val productCustomization: Customization? = null
) : TrackingEvent("productCustomization", siteId, clientId) {
    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this)

    companion object Properties {
        @Serializable
        data class Customization(
            val name: String,
            val value: String,
            val price: Double,
            val currency: String
        )
    }
}
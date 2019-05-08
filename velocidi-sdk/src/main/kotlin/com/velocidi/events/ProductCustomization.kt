package com.velocidi.events

data class ProductCustomization(
    override val siteId: String,
    override val clientId: String,
    val product: List<Product> = emptyList(),
    val productCustomization: Customization? = null
) : TrackingEvent("productCustomization") {
    override fun serialize(): String =
        defaultGson().toJson(this)

    companion object Properties {
        data class Customization(
            val name: String,
            val value: String? = null,
            val price: Double? = null,
            val currency: String? = null
        )
    }
}

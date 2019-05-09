package com.velocidi.events

data class ProductCustomization(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("productCustomization") {
    var product: List<Product> = emptyList()
    var productCustomization: Customization? = null

    companion object Properties {
        data class Customization(
            val name: String,
            val value: String? = null,
            val price: Double? = null,
            val currency: String? = null
        )
    }
}

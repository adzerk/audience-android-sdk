package com.velocidi.events

data class ProductCustomization(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("productCustomization") {
    var products: List<Product> = emptyList()
    var productCustomization: Customization? = null

    companion object Properties {
        data class Customization(
            val name: String
        ) {
            var value: String? = null
            var price: Double? = null
            var currency: String? = null
        }
    }
}

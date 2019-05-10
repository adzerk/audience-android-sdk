package com.velocidi.events

data class Product(
    val id: String
) {
    var name: String? = null
    var brand: String? = null
    var category: String? = null
    var variant: String? = null
    var parts: List<Product> = emptyList()
    var price: Double? = null
    var currency: String? = null
    var location: String? = null
    var position: Int? = null
    var quantity: Int? = null
    var recommendation: Boolean? = null
    var unsafe: Boolean? = null
}

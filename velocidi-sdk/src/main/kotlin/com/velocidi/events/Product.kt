package com.velocidi.events

/**
 * Product model
 *
 * @property id Product identifier
 */
data class Product(
    val id: String
) {
    /**
     * Product name
     */
    var name: String? = null

    /**
     * Product brand
     */
    var brand: String? = null

    /**
     * Product category
     */
    var category: String? = null

    /**
     * Product variant (e.g. color, size)
     */
    var variant: String? = null

    /**
     * Product parts or subproduct, when the product is an aggregation
     */
    var parts: List<Product> = emptyList()

    /**
     * Product price
     */
    var price: Double? = null

    /**
     * Product currency
     */
    var currency: String? = null

    /**
     * Location in the app where the product was seen (e.g. search results, favorites, related products)
     */
    var location: String? = null

    /**
     * Product position in the location (relevant when shown in a list)
     */
    var position: Int? = null

    /**
     * Quantity of the product
     */
    var quantity: Int? = null

    /**
     * Whether or not the product was suggested by a recommendation
     */
    var recommendation: Boolean? = null

    /**
     * Whether or not the product is unsafe (e.g. adult content)
     */
    var unsafe: Boolean? = null
}

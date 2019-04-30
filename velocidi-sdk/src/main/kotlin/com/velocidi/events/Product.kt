package com.velocidi.events

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    val name: String? = null,
    val brand: String? = null,
    val category: String? = null,
    val variant: String? = null,
    val parts: List<Product> = emptyList(),
    val price: Double? = null,
    val currency: String? = null,
    val location: String? = null,
    val position: Int? = null,
    val quantity: Int? = null,
    val recommendation: Boolean? = null,
    val unsafe: Boolean? = null
)

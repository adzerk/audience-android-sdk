package com.velocidi.events

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    @Optional val name: String? = null,
    @Optional val brand: String? = null,
    @Optional val category: String? = null,
    @Optional val variant: String? = null,
    @Optional val parts: List<Product> = emptyList(),
    @Optional val price: Double? = null,
    @Optional val currency: String? = null,
    @Optional val location: String? = null,
    @Optional val position: Int? = null,
    @Optional val quantity: Int? = null,
    @Optional val recommendation: Boolean? = null,
    @Optional val unsafe: Boolean? = null
)

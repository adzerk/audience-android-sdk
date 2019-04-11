package com.velocidi.events

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: String,
    @Optional val price: Double? = null,
    @Optional val recurrence: String? = null,
    @Optional val currency: String? = null,
    @Optional val tax: Double? = null,
    @Optional val shipping: Double? = null,
    @Optional val voucher: Voucher? = null,
    @Optional val paymentMethod: String? = null,
    @Optional val paymentDetails: String? = null
) {
    companion object Properties {
        @Serializable
        data class Voucher(val id: String, val percentage: Int, val value: Double)
    }
}
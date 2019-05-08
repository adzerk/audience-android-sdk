package com.velocidi.events

data class Transaction(
    val id: String,
    val price: Double? = null,
    val recurrence: String? = null,
    val currency: String? = null,
    val tax: Double? = null,
    val shipping: Double? = null,
    val voucher: Voucher? = null,
    val paymentMethod: String? = null,
    val paymentDetails: String? = null
) {
    companion object Properties {
        data class Voucher(
            val id: String,
            val percentage: Int? = null,
            val value: Double? = null
        )
    }
}

package com.velocidi.events

data class Transaction(
    val id: String
) {
    var price: Double? = null
    var recurrence: String? = null
    var currency: String? = null
    var tax: Double? = null
    var shipping: Double? = null
    var voucher: Voucher? = null
    var paymentMethod: String? = null
    var paymentDetails: String? = null

    companion object Properties {
        data class Voucher(
            val id: String
        ) {
            var percentage: Int? = null
            var value: Double? = null
        }
    }
}

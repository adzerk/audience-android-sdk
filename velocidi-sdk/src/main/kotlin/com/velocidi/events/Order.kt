package com.velocidi.events

/**
 * Order model
 *
 * @property id order id
 * @property currency order currency
 * @property total order total value (after taxes and shipping)
 * @property subtotal order subtotal
 * @property shipping order shipping cost
 * @property discount discount applied to the order
 * @property refund order refund value
 */
data class Order(
    val id: String,
    val currency: String,
    val total: Double,
    val tax: Double,
    val subtotal: Double,
    val shipping: Double,
    val discount: Discount,
    val refund: Double
) {
    /**
     * Payment type (e.g. credit card, debit card, Paypal)
     */
    var paymentMethod: String? = null

    /**
     * Shipping method
     */
    var shippingMethod: String? = null

    /**
     * Country for which the order was shipped
     */
    var shippingCountry: String? = null

    /**
     * Promotions or coupons used in the order.
     */
    var promotions: List<String> = emptyList()
}

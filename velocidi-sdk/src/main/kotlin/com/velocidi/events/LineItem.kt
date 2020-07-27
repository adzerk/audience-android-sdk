package com.velocidi.events

/**
 * Line item model
 *
 * @property id line item id
 * @property currency line item currency
 * @property total line item total value (after taxes and shipping)
 * @property subtotal line item subtotal
 * @property shipping line item shipping cost
 * @property refund line item refund value
 */
data class LineItem(
    val id: String,
    val currency: String,
    val total: Double,
    val subtotal: Double,
    val tax: Double,
    val shipping: Double
) {
    /**
     * Line item processed refund value
     */
    var refund: Double = 0.0

    /**
     * If this line item is a subproduct, this references the main product.
     */
    var itemGroupId: String? = null

    /**
     * The line item SKU
     */
    var sku: String? = null

    /**
     * If the product relates to a subscription, this represents the subscription duration, in days.
     */
    var subscriptionDuration: Int? = null

    /**
     * Line item id
     */
    var name: String? = null

    /**
     * Line item brand
     */
    var brand: String? = null

    /**
     * Line item type (category)
     */
    var productType: String? = null

    /**
     * Possible discount applied to the line item.
     */
    var discount: Discount? = null

    /**
     * Quantity of the line item
     */
    var quantity: Int = 1

    /**
     * Possible promotion or coupon used in the line item.
     */
    var promotion: String? = null

    /**
     * Whether or not the line item is adult-only.
     */
    var adult: Boolean = false
}

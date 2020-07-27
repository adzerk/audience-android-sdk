package com.velocidi.events

sealed class Discount

/**
 * Percentage discount, the percentage of the item value which is discount.
 */
data class PercentageDiscount(val percentage: Double) : Discount()

/**
 * Absolute value of the discount on the item.
 */
data class ValueDiscount(val value: Double, val currency: String) : Discount()

/**
 * Final price of the item after discount.
 */
data class PriceDiscount(val price: Double, val currency: String) : Discount()

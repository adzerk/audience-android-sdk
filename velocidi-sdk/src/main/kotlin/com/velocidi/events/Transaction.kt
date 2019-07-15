package com.velocidi.events

/**
 * Transaction model
 *
 * @property id Transaction identifier
 */
data class Transaction(
    val id: String
) {
    /**
     * Transaction total price (after taxes and shipping)
     */
    var price: Double? = null

    /**
     * Transaction frequency using cron format ( https://crontab.guru/ )
     */
    var recurrence: String? = null

    /**
     * Transaction currency
     */
    var currency: String? = null

    /**
     * Transaction tax
     */
    var tax: Double? = null

    /**
     * Transaction shipping cost
     */
    var shipping: Double? = null

    /**
     * Voucher used in this transaction
     */
    var voucher: Voucher? = null

    /**
     * Payment type (e.g. credit card, debit card, Paypal)
     */
    var paymentMethod: String? = null

    /**
     * Additional information about the payment
     */
    var paymentDetails: String? = null

    companion object Properties {

        /**
         * Voucher model
         *
         * @property id Identifier of the Voucher used
         */
        data class Voucher(
            val id: String
        ) {
            /**
             * Transaction voucher percentage
             */
            var percentage: Int? = null

            /**
             * Transaction voucher absolute discount
             */
            var value: Double? = null
        }
    }
}

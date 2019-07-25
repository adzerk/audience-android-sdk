package com.velocidi.events

/**
 * Product customization event
 *
 * @property siteId Client's site identifier
 * @property clientId Client identifier
 */
data class ProductCustomization(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("productCustomization") {

    /**
     * Products being customized
     */
    var products: List<Product> = emptyList()

    /**
     * Customizations being applied to the products
     */
    var productCustomization: Customization? = null

    companion object Properties {
        /**
         * Product customization model
         *
         * @property name Product customization name
         */
        data class Customization(
            val name: String
        ) {
            /**
             * Custom property to add extra information about the customization
             */
            var value: String? = null

            /**
             * Customization price
             */
            var price: Double? = null

            /**
             * Customization currency
             */
            var currency: String? = null
        }
    }
}

package com.velocidi

import com.velocidi.Util.toQueryParams
import com.velocidi.util.containsExactlyInAnyOrder
import org.assertj.core.api.Assertions.*
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TrackingEventsSerializationTest {
    private val defaultProduct =
        mapOf(
            "[itemGroupId]" to "p125",
            "[name]" to "My product",
            "[brand]" to "Nike",
            "[productType]" to "Shirts",
            "[currency]" to "EUR",
            "[promotions][0]" to "WINTERSALE",
            "[adult]" to "false",
            "[size]" to "S",
            "[sizeSystem]" to "EU",
            "[customFields][color]" to "blue",
            "[id]" to "p125zc",
            "[price]" to "102.5",
            "[gender]" to "male",
        )

    private val defaultProductObj =
        JSONObject(
            """
            {
              "itemGroupId": "p125",
              "name": "My product",
              "brand": "Nike",
              "productType": "Shirts",
              "currency": "EUR",
              "promotions": [
                "WINTERSALE"
              ],
              "adult": false,
              "size": "S",
              "sizeSystem": "EU",
              "customFields": {
                "color": "blue"
              },
              "id": "p125zc",
              "price": 102.5,
              "gender": "male"
            }
            """.trimIndent(),
        )

    private val defaultLineItem =
        mapOf(
            "[itemGroupId]" to "p125",
            "[name]" to "My product",
            "[brand]" to "Nike",
            "[productType]" to "Shirts",
            "[currency]" to "EUR",
            "[promotions][0]" to "WINTERSALE",
            "[adult]" to "false",
            "[size]" to "S",
            "[sizeSystem]" to "EU",
            "[customFields][color]" to "blue",
            "[lineItemId]" to "li125zc",
            "[productId]" to "p125zc",
            "[total]" to "102.5",
            "[subtotal]" to "85",
            "[tax]" to "11.5",
            "[shipping]" to "6",
            "[sku]" to "p125zc-5",
            "[discount][value]" to "5",
            "[discount][currency]" to "EUR",
            "[refund]" to "0",
            "[quantity]" to "1",
            "[subscriptionDuration]" to "180",
        )

    private val defaultLineItemObj =
        JSONObject(
            """
            {
              "itemGroupId": "p125",
              "name": "My product",
              "brand": "Nike",
              "productType": "Shirts",
              "currency": "EUR",
              "promotions": [
                "WINTERSALE"
              ],
              "adult": false,
              "size": "S",
              "sizeSystem": "EU",
              "customFields": {
                "color": "blue"
              },
              "lineItemId": "li125zc",
              "productId": "p125zc",
              "total": 102.5,
              "subtotal": 85,
              "tax": 11.5,
              "shipping": 6,
              "sku": "p125zc-5",
              "discount": {
                "value": 5,
                "currency": "EUR"
              },
              "refund": 0,
              "quantity": 1,
              "subscriptionDuration": 180
            }
            """.trimIndent(),
        )

    private val defaultOrder =
        mapOf(
            "order[id]" to "or123",
            "order[currency]" to "EUR",
            "order[total]" to "102.5",
            "order[subtotal]" to "85",
            "order[tax]" to "11.5",
            "order[shipping]" to "6",
            "order[discount][value]" to "5",
            "order[discount][currency]" to "EUR",
            "order[refund]" to "0",
            "order[paymentMethod]" to "Visa",
            "order[shippingMethod]" to "UPS",
            "order[shippingCountry]" to "France",
            "order[promotions][0]" to "WINTERSALE",
        )

    private val defaultOrderObj =
        JSONObject(
            """
            {
              "id": "or123",
              "currency": "EUR",
              "total": 102.5,
              "subtotal": 85,
              "tax": 11.5,
              "shipping": 6,
              "discount": {
                "value": 5,
                "currency": "EUR"
              },
              "refund": 0,
              "paymentMethod": "Visa",
              "shippingMethod": "UPS",
              "shippingCountry": "France",
              "promotions": [
                "WINTERSALE"
              ]
            }
            """.trimIndent(),
        )

    @Test
    fun pageViewEventSerialization() {
        val event =
            mapOf(
                "clientId" to "velocidi",
                "siteId" to "velocidi.com",
                "type" to "pageView",
                "customFields[debug]" to "true",
                "customFields[role]" to "superuser",
                "title" to "Welcome to your Shop",
                "pageType" to "homepage",
                "category" to "Shopping",
            )

        val eventObj =
            JSONObject(
                """
                {
                  "clientId": "velocidi",
                  "siteId": "velocidi.com",
                  "type": "pageView",
                  "customFields": {
                    "debug": "true",
                    "role": "superuser"
                  },
                  "title": "Welcome to your Shop",
                  "pageType": "homepage",
                  "category": "Shopping"
                }
                """.trimIndent(),
            )

        containsExactlyInAnyOrder(event, eventObj.toQueryParams())
    }

    @Test
    fun appViewEventSerialization() {
        val event =
            mapOf(
                "clientId" to "velocidi",
                "siteId" to "velocidi.com",
                "type" to "appView",
                "customFields[debug]" to "true",
                "customFields[role]" to "superuser",
                "title" to "Welcome Screen",
            )

        val eventObj =
            JSONObject(
                """
                {
                  "clientId": "velocidi",
                  "siteId": "velocidi.com",
                  "type": "appView",
                  "customFields": {
                    "debug": "true",
                    "role": "superuser"
                  },
                  "title": "Welcome Screen"
                }
                """.trimIndent(),
            )

        containsExactlyInAnyOrder(event, eventObj.toQueryParams())
    }

    @Test
    fun productImpressionEventSerialization() {
        val event =
            mutableMapOf(
                "clientId" to "velocidi",
                "siteId" to "velocidi.com",
                "type" to "productImpression",
                "customFields[debug]" to "true",
                "customFields[role]" to "superuser",
            )
        event.putAll(defaultProduct.mapKeys { (k, _) -> "products[0]$k" })

        val eventObj =
            JSONObject(
                """
                {
                  "clientId": "velocidi",
                  "siteId": "velocidi.com",
                  "type": "productImpression",
                  "customFields": {
                    "debug": "true",
                    "role": "superuser"
                  },
                  "products": [
                    $defaultProductObj
                  ]
                }
                """.trimIndent(),
            )

        containsExactlyInAnyOrder(event, eventObj.toQueryParams())
    }

    @Test
    fun addToCartEventSerialization() {
        val event =
            mutableMapOf(
                "clientId" to "velocidi",
                "siteId" to "velocidi.com",
                "type" to "addToCart",
                "customFields[debug]" to "true",
                "customFields[role]" to "superuser",
            )
        event.putAll(defaultProduct.mapKeys { (k, _) -> "products[0]$k" })

        val eventObj =
            JSONObject(
                """
                {
                  "clientId": "velocidi",
                  "siteId": "velocidi.com",
                  "type": "addToCart",
                  "customFields": {
                    "debug": "true",
                    "role": "superuser"
                  },
                  "products": [
                    $defaultProductObj
                  ]
                }
                """.trimIndent(),
            )

        containsExactlyInAnyOrder(event, eventObj.toQueryParams())
    }

    @Test
    fun orderPlaceEventSerialization() {
        val event =
            mutableMapOf(
                "clientId" to "velocidi",
                "siteId" to "velocidi.com",
                "type" to "orderPlace",
                "customFields[debug]" to "true",
                "customFields[role]" to "superuser",
            )
        event.putAll(defaultLineItem.mapKeys { (k, _) -> "lineItems[0]$k" })
        event.putAll(defaultOrder)

        val eventObj =
            JSONObject(
                """
                {
                  "clientId": "velocidi",
                  "siteId": "velocidi.com",
                  "type": "orderPlace",
                  "customFields": {
                    "debug": "true",
                    "role": "superuser"
                  },
                  "lineItems": [
                    $defaultLineItemObj
                  ],
                  "order": $defaultOrderObj
                }
                """.trimIndent(),
            )

        containsExactlyInAnyOrder(event, eventObj.toQueryParams())
    }

    @Test
    fun nullSerialization() {
        val event =
            mapOf(
                "clientId" to "velocidi",
                "siteId" to "velocidi.com",
                "type" to "pageView",
                "title" to "Welcome to your Shop",
                "pageType" to "homepage",
            )

        val eventObj =
            JSONObject(
                """
                {
                  "clientId": "velocidi",
                  "siteId": "velocidi.com",
                  "type": "pageView",
                  "title": "Welcome to your Shop",
                  "pageType": "homepage",
                  "category": null
                }
                """.trimIndent(),
            )

        containsExactlyInAnyOrder(event, eventObj.toQueryParams())
    }

    @Test
    fun nonPrimitiveTypeSerialization() {
        data class Foo(
            val bar: String,
        )

        val json = JSONObject()
        json.put("foo", Foo("bar"))

        containsExactlyInAnyOrder(emptyMap(), json.toQueryParams())
    }
}

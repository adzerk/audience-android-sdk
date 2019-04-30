package com.velocidi

import com.velocidi.events.*
import com.velocidi.util.prettyPrintJson
import org.junit.Test
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.assertj.core.api.Assertions.*
import org.json.JSONException
import org.json.JSONObject
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@ImplicitReflectionSerializer
class TrackingEventsTest {
    private val json = Json(JsonConfiguration.Stable.copy(encodeDefaults = false, prettyPrint = true))

    private val defaultProduct = """
        {
            "id": "p1",
            "name": "My product",
            "brand": "Velocidi",
            "category": "Clothes",
            "variant": "M",
            "parts": [
                {
                    "id": "p2"
                }
            ],
            "price": 12.99,
            "currency": "EUR",
            "quantity": 1,
            "recommendation": false,
            "unsafe": false
        }
  """

    private val defaultProductObj = Product(
        id = "p1",
        name = "My product",
        brand = "Velocidi",
        category = "Clothes",
        variant = "M",
        parts = listOf(Product(id = "p2")),
        price = 12.99,
        currency = "EUR",
        quantity = 1,
        recommendation = false,
        unsafe = false
    )

    @Test
    fun customTrackingEventFactory() {

        val event = """
        {
            "type": "custom",
            "siteId": "0",
            "clientId": "id1",
            "test": "testString"
        }"""

        val eventObj = CustomTrackingEventFactory.buildFromJSON(event)
        assertThat(eventObj.type).isEqualTo("custom")
        assertThat(eventObj.siteId).isEqualTo("0")
        assertThat(eventObj.clientId).isEqualTo("id1")
        assertThat(eventObj.extraAttributes.toString()).isEqualTo("""{"test":"testString"}""")
    }

    @Test(expected = JSONException::class)
    fun invalidCustomTrackingEvent() {
        val invalidEvent = """{"siteId": "0"}"""
        CustomTrackingEventFactory.buildFromJSON(invalidEvent)
    }

    @Test
    fun customTrackingEventSerialization() {
        val event =
            """
            {
                "test": {
                    "a": 1
                },
                "type": "custom",
                "siteId": "0",
                "clientId": "id1"
            }
            """.trimIndent()

        val eventObj = CustomTrackingEvent(
            "custom",
            "0",
            "id1",
            JSONObject(""" {"test": {"a": 1}} """)
        )

        assertThat(eventObj.serialize().prettyPrintJson()).isEqualTo(event.prettyPrintJson())
    }

    @Test
    fun pageViewEventSerialization() {
        val event =
            """
            {
                "type": "pageView",
                "siteId": "0",
                "clientId": "0",
                "location": "mylocation",
                "title": "My page",
                "pageType": "homepage",
                "category": "shopping"
            }
            """.trimIndent()

        val eventObj = PageView(
            siteId = "0",
            clientId = "0",
            location = "mylocation",
            title = "My page",
            pageType = "homepage",
            category = "shopping"
        )

        assertThat(event).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun searchEventSerialization() {
        val event =
            """
            {
                "type": "search",
                "siteId": "0",
                "clientId": "0",
                "query": "product"
            }
            """.trimIndent()

        val eventObj = Search(
            siteId = "0",
            clientId = "0",
            query = "product"
        )

        assertThat(event).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun productSerialization() {

        val emptyProduct =
            """
            {
                "id": "id1"
            }
            """.trimIndent()

        val obj = Product("id1")

        assertThat(defaultProduct.prettyPrintJson()).isEqualTo(json.stringify(defaultProductObj))

        assertThat(emptyProduct.prettyPrintJson()).isEqualTo(json.stringify(obj))
    }

    @Test
    fun productImpressionEventSerialization() {
        val event =
            """
            {
                "type": "productImpression",
                "siteId": "0",
                "clientId": "0",
                "products": [
                    $defaultProduct,
                    {
                        "id": "p2"
                    }
                ]
            }
            """.trimIndent()

        val eventObj = ProductImpression("0", "0", listOf(defaultProductObj, Product("p2")))

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun productClickEventSerialization() {
        val event =
            """
            {
                "type": "productClick",
                "siteId": "0",
                "clientId": "0",
                "products": [
                    $defaultProduct
                ],
            }
            """.trimIndent()

        val eventObj = ProductClick("0", "0", listOf(defaultProductObj))

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun productViewEventSerialization() {
        val event =
            """
            {
                "type": "productView",
                "siteId": "0",
                "clientId": "0",
                "products": [
                    $defaultProduct
                ],
            }
            """.trimIndent()

        val eventObj = ProductView("0", "0", listOf(defaultProductObj))

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun productViewDetailsEventSerialization() {
        val event =
            """
            {
                "type": "productViewDetails",
                "siteId": "0",
                "clientId": "0",
                "products": [
                    $defaultProduct
                ],
            }
            """.trimIndent()

        val eventObj = ProductViewDetails("0", "0", listOf(defaultProductObj))

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun productFeedbackEventSerialization() {
        val event =
            """
            {
                "type": "productFeedback",
                "siteId": "0",
                "clientId": "0",
                "products": [
                    $defaultProduct
                ],
                "rating": 4.5,
                "feedback": "It's a very nice product!"
            }
            """.trimIndent()

        val eventObj = ProductFeedback("0", "0", listOf(defaultProductObj), 4.5, "It's a very nice product!")

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun productCustomizationEventSerialization() {
        val event =
            """
            {
                "type": "productCustomization",
                "siteId": "0",
                "clientId": "0",
                "product": [
                    $defaultProduct
                ],
                "productCustomization": {
                    "name": "collar",
                    "value": "italian",
                    "price": 5.0,
                    "currency": "EUR"
                }
            }
            """.trimIndent()

        val customizationObj = ProductCustomization.Properties.Customization(
            name = "collar",
            value = "italian",
            price = 5.0,
            currency = "EUR"
        )

        val eventObj = ProductCustomization("0", "0", listOf(defaultProductObj), customizationObj)

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun addToCartEventSerialization() {
        val event =
            """
            {
                "type": "addToCart",
                "siteId": "0",
                "clientId": "0",
                "products": [
                    $defaultProduct
                ],
            }
            """.trimIndent()

        val eventObj = AddToCart("0", "0", listOf(defaultProductObj))

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun removeFromCartEventSerialization() {
        val event =
            """
            {
                "type": "removeFromCart",
                "siteId": "0",
                "clientId": "0",
                "products": [
                    $defaultProduct
                ],
            }
            """.trimIndent()

        val eventObj = RemoveFromCart("0", "0", listOf(defaultProductObj))

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun purchaseEventSerialization() {
        val event =
            """
            {
                "type": "purchase",
                "siteId": "0",
                "clientId": "0",
                "products": [
                    $defaultProduct
                ],
                "transaction": {
                    "id": "tr1",
                    "price": 15.59,
                    "recurrence": "0 0 1 * *",
                    "currency": "EUR",
                    "tax": 2.99,
                    "shipping": 4.59,
                    "voucher": {
                        "id": "WINTERSALE",
                        "percentage": 10,
                        "value": 5.0
                    },
                    "paymentMethod": "credit",
                    "paymentDetails": "Visa"
                }
            }
            """.trimIndent()

        val transactionObj = Transaction(
            id = "tr1",
            price = 15.59,
            recurrence = "0 0 1 * *",
            currency = "EUR",
            tax = 2.99,
            shipping = 4.59,
            voucher = Transaction.Properties.Voucher("WINTERSALE", 10, 5.0),
            paymentMethod = "credit",
            paymentDetails = "Visa"
        )

        val eventObj = Purchase("0", "0", listOf(defaultProductObj), transactionObj)

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun subscriptionEventSerialization() {
        val event =
            """
            {
                "type": "subscription",
                "siteId": "0",
                "clientId": "0",
                "products":[
                    $defaultProduct
                ],
                "transaction": {
                    "id": "tr1",
                    "price": 15.59,
                    "recurrence": "0 0 1 * *",
                    "currency": "EUR",
                    "tax": 2.99,
                    "shipping": 4.59,
                    "voucher": {
                        "id": "WINTERSALE",
                        "percentage": 10,
                        "value": 5.0
                    },
                    "paymentMethod": "credit",
                    "paymentDetails": "Visa"
                }
            }
            """.trimIndent()

        val transactionObj = Transaction(
            id = "tr1",
            price = 15.59,
            recurrence = "0 0 1 * *",
            currency = "EUR",
            tax = 2.99,
            shipping = 4.59,
            voucher = Transaction.Properties.Voucher("WINTERSALE", 10, 5.0),
            paymentMethod = "credit",
            paymentDetails = "Visa"
        )

        val eventObj = Subscription("0", "0", listOf(defaultProductObj), transactionObj)

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun refundEventSerialization() {
        val event =
            """
            {
                "type": "refund",
                "siteId": "0",
                "clientId": "0",
                "refundType": "partial",
                "products": [
                    $defaultProduct
                ],
                "transaction": {
                    "id": "tr1",
                    "price": 15.59,
                    "recurrence": "0 0 1 * *",
                    "currency": "EUR",
                    "tax": 2.99,
                    "shipping": 4.59,
                    "voucher": {
                        "id": "WINTERSALE",
                        "percentage": 10,
                        "value": 5.0
                    },
                    "paymentMethod": "credit",
                    "paymentDetails": "Visa"
                }
            }
            """.trimIndent()

        val transactionObj = Transaction(
            id = "tr1",
            price = 15.59,
            recurrence = "0 0 1 * *",
            currency = "EUR",
            tax = 2.99,
            shipping = 4.59,
            voucher = Transaction.Properties.Voucher("WINTERSALE", 10, 5.0),
            paymentMethod = "credit",
            paymentDetails = "Visa"
        )

        val eventObj = Refund("0", "0", "partial", listOf(defaultProductObj), transactionObj)

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }
}

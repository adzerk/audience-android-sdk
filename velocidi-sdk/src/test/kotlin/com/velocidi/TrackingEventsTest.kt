package com.velocidi

import com.velocidi.events.*
import com.velocidi.util.prettyPrintJson
import org.junit.Test
import org.assertj.core.api.Assertions.*
import org.json.JSONException
import org.json.JSONObject
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import com.google.gson.Gson
import com.google.gson.GsonBuilder

@RunWith(RobolectricTestRunner::class)
class TrackingEventsTest {
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
            "siteId": "0",
            "clientId": "id1",
            "test": "testString",
            "type": "custom"
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

        val gson = GsonBuilder().registerTypeAdapter(CustomTrackingEvent::class.java, CustomTrackingEventSerializer).create()
        println(gson.toJson(eventObj))

        assertThat(gson.toJson(eventObj).prettyPrintJson()).isEqualTo(event.prettyPrintJson())
    }

    @Test
    fun pageViewEventSerialization() {
        val gson = Gson()

        val event =
            """
            {
                "siteId": "0",
                "clientId": "0",
                "location": "mylocation",
                "title": "My page",
                "pageType": "homepage",
                "category": "shopping",
                "type": "pageView"
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

        assertThat(event.prettyPrintJson()).isEqualTo(gson.toJson(eventObj).prettyPrintJson())
    }

    @Test
    fun searchEventSerialization() {
        val event =
            """
            {
                "siteId": "0",
                "clientId": "0",
                "query": "product",
                "type": "search"
            }
            """.trimIndent()

        val eventObj = Search(
            siteId = "0",
            clientId = "0",
            query = "product"
        )

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
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

        val gson = GsonBuilder().registerTypeHierarchyAdapter(
            Collection::class.java,
            CollectionAdapter()
        ).create()

        assertThat(defaultProduct.prettyPrintJson()).isEqualTo(gson.toJson(defaultProductObj).prettyPrintJson())

        assertThat(emptyProduct.prettyPrintJson()).isEqualTo(gson.toJson(obj).prettyPrintJson())
    }

    @Test
    fun productImpressionEventSerialization() {
        val event =
            """
            {
                "siteId": "0",
                "clientId": "0",
                "products": [
                    $defaultProduct,
                    {
                        "id": "p2"
                    }
                ],
                "type": "productImpression"
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
                "siteId": "0",
                "clientId": "0",
                "products": [
                    $defaultProduct
                ],
                "type": "productClick"
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
                "siteId": "0",
                "clientId": "0",
                "products": [
                    $defaultProduct
                ],
                "type": "productView"
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
                "siteId": "0",
                "clientId": "0",
                "products": [
                    $defaultProduct
                ],
                "type": "productViewDetails"
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
                "siteId": "0",
                "clientId": "0",
                "products": [
                    $defaultProduct
                ],
                "rating": 4.5,
                "feedback": "It's a very nice product!",
                "type": "productFeedback"
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
                },
                "type": "productCustomization"
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
                "siteId": "0",
                "clientId": "0",
                "products": [
                    $defaultProduct
                ],
                "type": "addToCart"
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
                "siteId": "0",
                "clientId": "0",
                "products": [
                    $defaultProduct
                ],
                "type": "removeFromCart"
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
                },
                "type": "purchase"
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
                },
                "type": "subscription"
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
                },
                "type": "refund"
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

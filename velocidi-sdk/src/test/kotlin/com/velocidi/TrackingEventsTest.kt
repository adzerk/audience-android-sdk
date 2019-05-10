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

@RunWith(RobolectricTestRunner::class)
class TrackingEventsTest {
    private val defaultProduct = """
        {
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
            "unsafe": false,
            "id": "p1"
        }
  """

    private val defaultProductObj = Product(id = "p1")

    init {
        defaultProductObj.name = "My product"
        defaultProductObj.brand = "Velocidi"
        defaultProductObj.category = "Clothes"
        defaultProductObj.variant = "M"
        defaultProductObj.parts = listOf(Product(id = "p2"))
        defaultProductObj.price = 12.99
        defaultProductObj.currency = "EUR"
        defaultProductObj.quantity = 1
        defaultProductObj.recommendation = false
        defaultProductObj.unsafe = false
    }

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

        assertThat(eventObj.serialize().prettyPrintJson()).isEqualTo(event.prettyPrintJson())
    }

    @Test
    fun pageViewEventSerialization() {
        val gson = Gson()

        val event =
            """
            {
                "location": "mylocation",
                "title": "My page",
                "pageType": "homepage",
                "category": "shopping",
                "siteId": "0",
                "clientId": "0",
                "type": "pageView"
            }
            """.trimIndent()

        val eventObj = PageView(
            siteId = "0",
            clientId = "0"
        )
        eventObj.location = "mylocation"
        eventObj.title = "My page"
        eventObj.pageType = "homepage"
        eventObj.category = "shopping"

        assertThat(event.prettyPrintJson()).isEqualTo(gson.toJson(eventObj).prettyPrintJson())
    }

    @Test
    fun searchEventSerialization() {
        val event =
            """
            {
                "query": "product",
                "siteId": "0",
                "clientId": "0",
                "type": "search"
            }
            """.trimIndent()

        val eventObj = Search(
            siteId = "0",
            clientId = "0"
        )
        eventObj.query = "product"

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

        val gson = TrackingEvent.defaultGson

        assertThat(defaultProduct.prettyPrintJson()).isEqualTo(gson.toJson(defaultProductObj).prettyPrintJson())

        assertThat(emptyProduct.prettyPrintJson()).isEqualTo(gson.toJson(obj).prettyPrintJson())
    }

    @Test
    fun productImpressionEventSerialization() {
        val event =
            """
            {
                "products": [
                    $defaultProduct,
                    {
                        "id": "p2"
                    }
                ],
                "siteId": "0",
                "clientId": "0",
                "type": "productImpression"
            }
            """.trimIndent()

        val eventObj = ProductImpression(
            siteId = "0",
            clientId = "0"
        )
        eventObj.products = listOf(defaultProductObj, Product("p2"))

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun productClickEventSerialization() {
        val event =
            """
            {
                "products": [
                    $defaultProduct
                ],
                "siteId": "0",
                "clientId": "0",
                "type": "productClick"
            }
            """.trimIndent()

        val eventObj = ProductClick(
            siteId = "0",
            clientId = "0"
        )
        eventObj.products = listOf(defaultProductObj)

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun productViewEventSerialization() {
        val event =
            """
            {
                "products": [
                    $defaultProduct
                ],
                "siteId": "0",
                "clientId": "0",
                "type": "productView"
            }
            """.trimIndent()

        val eventObj = ProductView(
            siteId = "0",
            clientId = "0"
        )
        eventObj.products = listOf(defaultProductObj)

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun productViewDetailsEventSerialization() {
        val event =
            """
            {
                "products": [
                    $defaultProduct
                ],
                "siteId": "0",
                "clientId": "0",
                "type": "productViewDetails"
            }
            """.trimIndent()

        val eventObj = ProductViewDetails(
            siteId = "0",
            clientId = "0"
        )
        eventObj.products = listOf(defaultProductObj)

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun productFeedbackEventSerialization() {
        val event =
            """
            {
                "products": [
                    $defaultProduct
                ],
                "rating": 4.5,
                "feedback": "It's a very nice product!",
                "siteId": "0",
                "clientId": "0",
                "type": "productFeedback"
            }
            """.trimIndent()

        val eventObj = ProductFeedback(
            siteId = "0",
            clientId = "0"
        )
        eventObj.products = listOf(defaultProductObj)
        eventObj.rating = 4.5
        eventObj.feedback = "It's a very nice product!"

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun productCustomizationEventSerialization() {
        val event =
            """
            {
                "product": [
                    $defaultProduct
                ],
                "productCustomization": {
                    "value": "italian",
                    "price": 5.0,
                    "currency": "EUR",
                    "name": "collar"
                },
                "siteId": "0",
                "clientId": "0",
                "type": "productCustomization"
            }
            """.trimIndent()

        val customizationObj = ProductCustomization.Properties.Customization(
            name = "collar"
        )
        customizationObj.value = "italian"
        customizationObj.price = 5.0
        customizationObj.currency = "EUR"

        val eventObj = ProductCustomization(
            siteId = "0",
            clientId = "0"
        )
        eventObj.product = listOf(defaultProductObj)
        eventObj.productCustomization = customizationObj

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun addToCartEventSerialization() {
        val event =
            """
            {
                "products": [
                    $defaultProduct
                ],
                "siteId": "0",
                "clientId": "0",
                "type": "addToCart"
            }
            """.trimIndent()

        val eventObj = AddToCart(
            siteId = "0",
            clientId = "0"
        )
        eventObj.products = listOf(defaultProductObj)

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun removeFromCartEventSerialization() {
        val event =
            """
            {
                "products": [
                    $defaultProduct
                ],
                "siteId": "0",
                "clientId": "0",
                "type": "removeFromCart"
            }
            """.trimIndent()

        val eventObj = RemoveFromCart(
            siteId = "0",
            clientId = "0"
        )
        eventObj.products = listOf(defaultProductObj)

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun purchaseEventSerialization() {
        val event =
            """
            {
                "products": [
                    $defaultProduct
                ],
                "transaction": {
                    "price": 15.59,
                    "recurrence": "0 0 1 * *",
                    "currency": "EUR",
                    "tax": 2.99,
                    "shipping": 4.59,
                    "voucher": {
                        "percentage": 10,
                        "value": 5.0,
                        "id": "WINTERSALE"
                    },
                    "paymentMethod": "credit",
                    "paymentDetails": "Visa",
                    "id": "tr1"
                },
                "siteId": "0",
                "clientId": "0",
                "type": "purchase"
            }
            """.trimIndent()

        val voucher = Transaction.Properties.Voucher("WINTERSALE")
        voucher.percentage = 10
        voucher.value = 5.0

        val transactionObj = Transaction(
            id = "tr1"
        )
        transactionObj.price = 15.59
        transactionObj.recurrence = "0 0 1 * *"
        transactionObj.currency = "EUR"
        transactionObj.tax = 2.99
        transactionObj.shipping = 4.59
        transactionObj.voucher = voucher
        transactionObj.paymentMethod = "credit"
        transactionObj.paymentDetails = "Visa"

        val eventObj = Purchase(
            siteId = "0",
            clientId = "0"
        )
        eventObj.products = listOf(defaultProductObj)
        eventObj.transaction = transactionObj

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun subscriptionEventSerialization() {
        val event =
            """
            {
                "products":[
                    $defaultProduct
                ],
                "transaction": {
                    "price": 15.59,
                    "recurrence": "0 0 1 * *",
                    "currency": "EUR",
                    "tax": 2.99,
                    "shipping": 4.59,
                    "voucher": {
                        "percentage": 10,
                        "value": 5.0,
                        "id": "WINTERSALE"
                    },
                    "paymentMethod": "credit",
                    "paymentDetails": "Visa",
                    "id": "tr1"
                },
                "siteId": "0",
                "clientId": "0",
                "type": "subscription"
            }
            """.trimIndent()

        val voucher = Transaction.Properties.Voucher("WINTERSALE")
        voucher.percentage = 10
        voucher.value = 5.0

        val transactionObj = Transaction(
            id = "tr1"
        )
        transactionObj.price = 15.59
        transactionObj.recurrence = "0 0 1 * *"
        transactionObj.currency = "EUR"
        transactionObj.tax = 2.99
        transactionObj.shipping = 4.59
        transactionObj.voucher = voucher
        transactionObj.paymentMethod = "credit"
        transactionObj.paymentDetails = "Visa"

        val eventObj = Subscription(
            siteId = "0",
            clientId = "0"
        )
        eventObj.products = listOf(defaultProductObj)
        eventObj.transaction = transactionObj

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }

    @Test
    fun refundEventSerialization() {
        val event =
            """
            {
                "products": [
                    $defaultProduct
                ],
                "transaction": {
                    "price": 15.59,
                    "recurrence": "0 0 1 * *",
                    "currency": "EUR",
                    "tax": 2.99,
                    "shipping": 4.59,
                    "voucher": {
                        "percentage": 10,
                        "value": 5.0,
                        "id": "WINTERSALE"
                    },
                    "paymentMethod": "credit",
                    "paymentDetails": "Visa",
                    "id": "tr1"
                },
                "siteId": "0",
                "clientId": "0",
                "refundType": "partial",
                "type": "refund"
            }
            """.trimIndent()

        val voucher = Transaction.Properties.Voucher("WINTERSALE")
        voucher.percentage = 10
        voucher.value = 5.0

        val transactionObj = Transaction(
            id = "tr1"
        )
        transactionObj.price = 15.59
        transactionObj.recurrence = "0 0 1 * *"
        transactionObj.currency = "EUR"
        transactionObj.tax = 2.99
        transactionObj.shipping = 4.59
        transactionObj.voucher = voucher
        transactionObj.paymentMethod = "credit"
        transactionObj.paymentDetails = "Visa"

        val eventObj = Refund(
            siteId = "0",
            clientId = "0",
            refundType = "partial"
        )
        eventObj.products = listOf(defaultProductObj)
        eventObj.transaction = transactionObj

        assertThat(event.prettyPrintJson()).isEqualTo(eventObj.serialize().prettyPrintJson())
    }
}

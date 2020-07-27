package com.velocidi

import com.velocidi.events.*
import org.junit.Test
import org.assertj.core.api.Assertions.*
import org.json.JSONException
import org.json.JSONObject
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TrackingEventsTest {
    private val defaultProduct = mapOf(
        "[name]" to "My product",
        "[brand]" to "Velocidi",
        "[category]" to "Clothes",
        "[variant]" to "M",
        "[parts][0][id]" to "p2",
        "[price]" to "12.99",
        "[currency]" to "EUR",
        "[quantity]" to "1",
        "[recommendation]" to "false",
        "[unsafe]" to "false",
        "[id]" to "p1"
    )

    private val defaultProductObj = Product(id = "p1").also {
        it.name = "My product"
        it.brand = "Velocidi"
        it.category = "Clothes"
        it.variant = "M"
        it.parts = listOf(Product(id = "p2"))
        it.price = 12.99
        it.currency = "EUR"
        it.quantity = 1
        it.recommendation = false
        it.unsafe = false
    }

    private val defaultTransaction = mapOf(
        "transaction[price]" to "15.59",
        "transaction[recurrence]" to "0 0 1 * *",
        "transaction[currency]" to "EUR",
        "transaction[tax]" to "2.99",
        "transaction[shipping]" to "4.59",
        "transaction[paymentMethod]" to "credit",
        "transaction[paymentDetails]" to "Visa",
        "transaction[id]" to "tr1",
        "transaction[voucher][percentage]" to "10",
        "transaction[voucher][value]" to "5.0",
        "transaction[voucher][id]" to "WINTERSALE"
    )

    private val defaultTransactionObj = Transaction(id = "tr1").also {
        val voucher = Transaction.Properties.Voucher("WINTERSALE")
        voucher.percentage = 10
        voucher.value = 5.0

        it.price = 15.59
        it.recurrence = "0 0 1 * *"
        it.currency = "EUR"
        it.tax = 2.99
        it.shipping = 4.59
        it.voucher = voucher
        it.paymentMethod = "credit"
        it.paymentDetails = "Visa"
    }

    private val defaultLineItem = mapOf(
        "[name]" to "My product",
        "[brand]" to "Velocidi",
        "[productType]" to "Clothes",
        "[currency]" to "EUR",
        "[total]" to "12.99",
        "[subtotal]" to "85.0",
        "[tax]" to "14.17",
        "[shipping]" to "11.0",
        "[refund]" to "0.0",
        "[quantity]" to "1",
        "[itemGroupId]" to "liGroup1",
        "[discount][percentage]" to "0.1",
        "[adult]" to "false",
        "[id]" to "p1",
        "[subscriptionDuration]" to "30"
    )

    private val defaultLineItemObj =
        LineItem(
            id = "p1",
            currency = "EUR",
            total = 12.99,
            subtotal = 85.0,
            tax = 14.17,
            shipping = 11.0
        ).also {
            it.refund = 0.0
            it.name = "My product"
            it.productType = "Clothes"
            it.brand = "Velocidi"
            it.discount = PercentageDiscount(0.1)
            it.itemGroupId = "liGroup1"
            it.subscriptionDuration = 30
        }

    private val defaultOrder = mapOf(
        "order[id]" to "or123",
        "order[currency]" to "USD",
        "order[total]" to "24.99",
        "order[subtotal]" to "16.94",
        "order[tax]" to "5.06",
        "order[shipping]" to "2.99",
        "order[discount][value]" to "2.0",
        "order[discount][currency]" to "USD",
        "order[refund]" to "0.0",
        "order[paymentMethod]" to "Visa",
        "order[shippingMethod]" to "DHL",
        "order[shippingCountry]" to "France",
        "order[promotions][0]" to "WINTERSALE"
    )

    private val defaultOrderObj =
        Order(
            id = "or123",
            currency = "USD",
            total = 24.99,
            tax = 5.06,
            subtotal = 16.94,
            shipping = 2.99,
            discount = ValueDiscount(2.0, "USD"),
            refund = 0.0
        ).also {
            it.paymentMethod = "Visa"
            it.shippingMethod = "DHL"
            it.shippingCountry = "France"
            it.promotions = listOf("WINTERSALE")
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
        val event = mapOf(
            "test[a]" to "1",
            "type" to "custom",
            "siteId" to "0",
            "clientId" to "id1"
        )

        val eventObj = CustomTrackingEvent(
            "custom",
            "0",
            "id1",
            JSONObject(""" {"test": {"a": 1}} """)
        )

        assertThat(event).isEqualTo(eventObj.toQueryParams())
    }

    @Test
    fun customTrackingEventAppendPropriety() {
        val event = mapOf(
            "type" to "custom",
            "siteId" to "0",
            "clientId" to "id1",
            "foo" to "bar"
        )

        val eventObj = CustomTrackingEvent(
            "custom",
            "0",
            "id1"
        )
        eventObj.appendProperty("foo", "bar")

        assertThat(event).isEqualTo(eventObj.toQueryParams())
    }

    @Test
    fun pageViewEventSerialization() {
        val event = mapOf(
            "type" to "pageView",
            "siteId" to "0",
            "clientId" to "0",
            "location" to "mylocation",
            "title" to "My page",
            "pageType" to "homepage",
            "category" to "shopping"
        )

        val eventObj = PageView(
            siteId = "0",
            clientId = "0"
        )
        eventObj.location = "mylocation"
        eventObj.title = "My page"
        eventObj.pageType = "homepage"
        eventObj.category = "shopping"

        assertThat(event).isEqualTo(eventObj.toQueryParams())
    }

    @Test
    fun appViewEventSerialization() {
        val event = mapOf(
            "type" to "appView",
            "siteId" to "0",
            "clientId" to "0",
            "title" to "landing"
        )

        val eventObj = AppView(
            siteId = "0",
            clientId = "0",
            title = "landing"
        )

        assertThat(event).isEqualTo(eventObj.toQueryParams())
    }

    @Test
    fun searchEventSerialization() {
        val event = mapOf(
            "type" to "search",
            "siteId" to "0",
            "clientId" to "0",
            "query" to "product"
        )

        val eventObj = Search(
            siteId = "0",
            clientId = "0"
        )
        eventObj.query = "product"

        assertThat(event).isEqualTo(eventObj.toQueryParams())
    }

    @Test
    fun productImpressionEventSerialization() {
        val event = mutableMapOf(
            "siteId" to "0",
            "clientId" to "0",
            "type" to "productImpression"
        )
        event.putAll(defaultProduct.mapKeys { (k, _) -> "products[0]$k" })
        event.put("products[1][id]", "p2")

        val eventObj = ProductImpression(
            siteId = "0",
            clientId = "0"
        )
        eventObj.products = listOf(defaultProductObj, Product("p2"))

        assertThat(event).isEqualTo(eventObj.toQueryParams())
    }

    @Test
    fun productClickEventSerialization() {
        val event = mutableMapOf(
            "siteId" to "0",
            "clientId" to "0",
            "type" to "productClick"
        )
        event.putAll(defaultProduct.mapKeys { (k, _) -> "products[0]$k" })

        val eventObj = ProductClick(
            siteId = "0",
            clientId = "0"
        )
        eventObj.products = listOf(defaultProductObj)

        assertThat(event).isEqualTo(eventObj.toQueryParams())
    }

    @Test
    fun productViewEventSerialization() {
        val event = mutableMapOf(
            "siteId" to "0",
            "clientId" to "0",
            "type" to "productView"
        )
        event.putAll(defaultProduct.mapKeys { (k, _) -> "products[0]$k" })

        val eventObj = ProductView(
            siteId = "0",
            clientId = "0"
        )
        eventObj.products = listOf(defaultProductObj)

        assertThat(event).isEqualTo(eventObj.toQueryParams())
    }

    @Test
    fun productViewDetailsEventSerialization() {

        val event = mutableMapOf(
            "siteId" to "0",
            "clientId" to "0",
            "type" to "productViewDetails"
        )
        event.putAll(defaultProduct.mapKeys { (k, _) -> "products[0]$k" })

        val eventObj = ProductViewDetails(
            siteId = "0",
            clientId = "0"
        )
        eventObj.products = listOf(defaultProductObj)

        assertThat(event).isEqualTo(eventObj.toQueryParams())
    }

    @Test
    fun productFeedbackEventSerialization() {
        val event = mutableMapOf(
            "rating" to "4.5",
            "feedback" to "It's a very nice product!",
            "siteId" to "0",
            "clientId" to "0",
            "type" to "productFeedback"
        )
        event.putAll(defaultProduct.mapKeys { (k, _) -> "products[0]$k" })

        val eventObj = ProductFeedback(
            siteId = "0",
            clientId = "0"
        )
        eventObj.products = listOf(defaultProductObj)
        eventObj.rating = 4.5
        eventObj.feedback = "It's a very nice product!"

        assertThat(event).isEqualTo(eventObj.toQueryParams())
    }

    @Test
    fun productCustomizationEventSerialization() {
        val event = mutableMapOf(
            "productCustomization[value]" to "italian",
            "productCustomization[price]" to "5.0",
            "productCustomization[currency]" to "EUR",
            "productCustomization[name]" to "collar",
            "siteId" to "0",
            "clientId" to "0",
            "type" to "productCustomization"
        )
        event.putAll(defaultProduct.mapKeys { (k, _) -> "products[0]$k" })

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
        eventObj.products = listOf(defaultProductObj)
        eventObj.productCustomization = customizationObj

        assertThat(event).isEqualTo(eventObj.toQueryParams())
    }

    @Test
    fun addToCartEventSerialization() {
        val event = mutableMapOf(
            "siteId" to "0",
            "clientId" to "0",
            "type" to "addToCart"
        )
        event.putAll(defaultProduct.mapKeys { (k, _) -> "products[0]$k" })

        val eventObj = AddToCart(
            siteId = "0",
            clientId = "0"
        )
        eventObj.products = listOf(defaultProductObj)

        assertThat(event).isEqualTo(eventObj.toQueryParams())
    }

    @Test
    fun removeFromCartEventSerialization() {
        val event = mutableMapOf(
            "siteId" to "0",
            "clientId" to "0",
            "type" to "removeFromCart"
        )
        event.putAll(defaultProduct.mapKeys { (k, _) -> "products[0]$k" })

        val eventObj = RemoveFromCart(
            siteId = "0",
            clientId = "0"
        )
        eventObj.products = listOf(defaultProductObj)

        assertThat(event).isEqualTo(eventObj.toQueryParams())
    }

    @Test
    fun purchaseEventSerialization() {
        val event = mutableMapOf(
            "siteId" to "0",
            "clientId" to "0",
            "type" to "purchase"
        )
        event.putAll(defaultProduct.mapKeys { (k, _) -> "products[0]$k" })
        event.putAll(defaultTransaction)

        val eventObj = Purchase(
            siteId = "0",
            clientId = "0"
        )
        eventObj.products = listOf(defaultProductObj)
        eventObj.transaction = defaultTransactionObj

        assertThat(event).isEqualTo(eventObj.toQueryParams())
    }

    @Test
    fun orderPlaceEventSerialization() {
        val event = mutableMapOf(
            "siteId" to "0",
            "clientId" to "0",
            "type" to "orderPlace"
        )
        event.putAll(defaultLineItem.mapKeys { (k, _) -> "lineItems[0]$k" })
        event.putAll(defaultOrder)

        val eventObj = OrderPlace(
            siteId = "0",
            clientId = "0"
        )
        eventObj.lineItems = listOf(defaultLineItemObj)
        eventObj.order = defaultOrderObj

        assertThat(event).isEqualTo(eventObj.toQueryParams())
    }

    @Test
    fun refundEventSerialization() {
        val event = mutableMapOf(
            "siteId" to "0",
            "clientId" to "0",
            "refundType" to "partial",
            "type" to "refund"
        )
        event.putAll(defaultProduct.mapKeys { (k, _) -> "products[0]$k" })
        event.putAll(defaultTransaction)

        val eventObj = Refund(
            siteId = "0",
            clientId = "0",
            refundType = "partial"
        )
        eventObj.products = listOf(defaultProductObj)
        eventObj.transaction = defaultTransactionObj

        assertThat(event).isEqualTo(eventObj.toQueryParams())
    }
}

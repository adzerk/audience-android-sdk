package com.velocidi

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import org.json.JSONObject

@Serializable
abstract class TrackingEvent(
    val type: String,
    @Transient open val siteId: String? = null,
    @Transient open val clientId: String? = null
)

@Serializable
class PageView(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val location: String? = null,
    @Optional val title: String? = null,
    @Optional val pageType: String? = null,
    @Optional val category: String? = null
) : TrackingEvent("pageView", siteId, clientId)

@Serializable
data class Search(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val query: String? = null
) : TrackingEvent("search", siteId, clientId)

@Serializable
data class Product(
    val id: String,
    @Optional val name: String? = null,
    @Optional val brand: String? = null,
    @Optional val category: String? = null,
    @Optional val variant: String? = null,
    @Optional val parts: List<Product> = emptyList(),
    @Optional val price: Double? = null,
    @Optional val currency: String? = null,
    @Optional val location: String? = null,
    @Optional val position: Int? = null,
    @Optional val quantity: Int? = null,
    @Optional val recommendation: Boolean? = null,
    @Optional val unsafe: Boolean? = null
)

@Serializable
data class ProductImpression(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val products: List<Product>? = emptyList()
) : TrackingEvent("productImpression", siteId, clientId)

@Serializable
data class ProductClick(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val product: Product? = null
) : TrackingEvent("productClick", siteId, clientId)

@Serializable
data class ProductView(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val product: Product? = null
) : TrackingEvent("productView", siteId, clientId)

@Serializable
data class ProductViewDetails(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val product: Product? = null
) : TrackingEvent("productViewDetails", siteId, clientId)

@Serializable
data class ProductFeedback(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val product: Product? = null,
    @Optional val rating: Double? = null,
    @Optional val feedback: String? = null
) : TrackingEvent("productFeedback", siteId, clientId)

@Serializable
data class ProductCustomization(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val product: Product? = null,
    @Optional val productCustomization: Customization? = null
) : TrackingEvent("productCustomization", siteId, clientId) {
    companion object Properties {
        @Serializable
        data class Customization(val name: String, val value: String, val price: Double, val currency: String)
    }
}

@Serializable
data class AddToCart(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val product: Product? = null
) : TrackingEvent("addToCart", siteId, clientId)

@Serializable
data class RemoveFromCart(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val product: Product? = null
) : TrackingEvent("removeFromCart", siteId, clientId)

@Serializable
data class Purchase(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val product: Product? = null,
    @Optional val transaction: Transaction? = null
) : TrackingEvent("purchase", siteId, clientId)

@Serializable
data class Transaction(
    val id: String,
    @Optional val price: Double? = null,
    @Optional val recurrence: String? = null,
    @Optional val currency: String? = null,
    @Optional val tax: Double? = null,
    @Optional val shipping: Double? = null,
    @Optional val voucher: Voucher? = null,
    @Optional val paymentMethod: String? = null,
    @Optional val paymentDetails: String? = null
) {
    companion object Properties {
        @Serializable
        data class Voucher(val id: String, val percentage: Int, val value: Double)
    }
}

@Serializable
data class Subscription(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val product: Product? = null,
    @Optional val transaction: Transaction? = null
) : TrackingEvent("subscription", siteId, clientId)

@Serializable
data class Refund(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    val refundType: String,
    @Optional val products: List<Product>? = emptyList(),
    @Optional val transaction: Transaction? = null
) : TrackingEvent("refund", siteId, clientId)


@Serializable
class CustomTrackingEvent(@Transient val json: JSONObject = JSONObject()) :
    TrackingEvent(json.getString("type"), extractAttribute(json, "siteId"), extractAttribute(json, "clientId")) {

    @Serializer(forClass = CustomTrackingEvent::class)
    companion object : SerializationStrategy<CustomTrackingEvent> {
        override val descriptor: SerialDescriptor = StringDescriptor.withName("CustomTrackingEvent")

        override fun serialize(encoder: Encoder, obj: CustomTrackingEvent) {
            encoder.encodeString(obj.json.toString())
        }

        fun extractAttribute(json: JSONObject, attr: String): String? {
            return try {
                json.getString(attr)
            } catch (e: org.json.JSONException) {
                null
            }
        }

    }
}

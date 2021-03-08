package com.velocidi

/**
 * Data model of a user identifier
 *
 * @property type type of identifier (defaults to "gaid")
 * @property id the identifier
 * @see <a href="https://docs.velocidi.com/collect/user-ids">https://docs.velocidi.com/collect/user-ids</a>
 */
data class UserId(val id: String, val type: String = "gaid") {
    fun toPair(): Pair<String, String> = Pair("id_$type", id)
}

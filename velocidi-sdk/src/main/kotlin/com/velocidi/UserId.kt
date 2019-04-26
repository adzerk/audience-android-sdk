package com.velocidi

/**
 * Data model of a user identifier
 *
 * @property type type of identifier (e.g. EML)
 * @property id the identifier
 */
data class UserId(val type: String, val id: String)

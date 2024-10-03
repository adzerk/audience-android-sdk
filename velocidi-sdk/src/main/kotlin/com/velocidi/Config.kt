package com.velocidi

import android.net.Uri

/**
 * Class with all the necessary logic to correctly initialize and customize the SDK
 *
 * @property track Channel configuration for the track functionality
 * @property match Channel configuration for the match functionality
 */
class Config(
    val track: Channel,
    val match: Channel,
) {
    constructor(url: Uri) : this(
        Channel(parseUrl(url, "tr", "events"), true),
        Channel(parseUrl(url, "match", "match"), true),
    )

    internal companion object {
        private fun parseUrl(
            url: Uri,
            prefix: String,
            endpoint: String,
        ): Uri =
            Uri
                .Builder()
                .scheme(url.scheme)
                .encodedAuthority("$prefix.${url.host}")
                .appendEncodedPath(endpoint)
                .build()
    }
}

/**
 * Data model for the configuration of a channel
 *
 * @property host endpoint URL
 * @property enabled
 */
data class Channel(
    val host: Uri,
    val enabled: Boolean,
)

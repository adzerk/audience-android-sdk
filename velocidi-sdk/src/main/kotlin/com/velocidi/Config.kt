package com.velocidi

import java.net.URL

/**
 * Class with all the necessary logic to correctly initialize and customize the SDK
 *
 * @property track Channel configuration for the track functionality
 * @property match Channel configuration for the match functionality
 */
class Config(val track: Channel, val match: Channel) {

    constructor(url: URL) : this(Channel(parseUrl(url, "tr", "events"), true), Channel(parseUrl(url, "match", "match"), true))

    companion object {

        private fun parseUrl(url: URL, prefix: String, endpoint: String): URL =
            URL(url.protocol, "$prefix.${url.host}", url.port, "/$endpoint")
    }
}

/**
 * Data model for the configuration of a channel
 *
 * @property host endpoint URL
 * @property enabled
 */
data class Channel(val host: URL, val enabled: Boolean)

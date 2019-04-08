package com.velocidi

import java.net.URL

class Config(val track: Channel, val match: Channel) {


    constructor(url: URL): this(Channel(parseUrl(url, "tr"), true), Channel(parseUrl(url,"match"), true))

    companion object {

        private fun parseUrl(url: URL, prefix: String): URL =
            URL(url.protocol, "$prefix.${url.host}", url.port, url.file)

    }
}

data class Channel(val host: URL, val enabled: Boolean)

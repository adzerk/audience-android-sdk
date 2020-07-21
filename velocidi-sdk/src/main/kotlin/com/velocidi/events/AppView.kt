package com.velocidi.events

/**
 * Page view event
 *
 * @property siteId Client's site identifier
 * @property clientId Client identifier
 * @property title Title of the page being viewed
 */
class AppView(
    override val siteId: String,
    override val clientId: String,
    val title: String
) : TrackingEvent("appView")

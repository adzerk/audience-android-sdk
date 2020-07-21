package com.velocidi.events

/**
 * Page view event
 *
 * @property siteId Client's site identifier
 * @property clientId Client identifier
 * @property location Address of the page being viewed
 */
class AppView(
    override val siteId: String,
    override val clientId: String,
    val location: String
) : TrackingEvent("appView") {

    /**
     * Page title
     */
    var title: String? = null

    /**
     * Page type (e.g. homepage)
     */
    var pageType: String? = null

    /**
     * Page category
     */
    var category: String? = null
}

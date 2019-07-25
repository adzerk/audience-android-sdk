package com.velocidi.events

/**
 * Page view event
 *
 * @property siteId Client's site identifier
 * @property clientId Client identifier
 */
class PageView(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("pageView") {

    /**
     * Address of the page being viewed
     */
    var location: String? = null

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

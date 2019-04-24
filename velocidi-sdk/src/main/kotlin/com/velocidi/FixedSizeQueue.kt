package com.velocidi

import java.util.LinkedList

/**
 * Implementation of a max size queue based on LinkedList
 * Once the defined limit is reached, older elements are removed.
 *
 * @param E type of elements held in the queue
 * @property limit queue max size
 */
class FixedSizeQueue<E>(private val limit: Int) : LinkedList<E>() {

    override fun add(o: E): Boolean {
        val added = super.add(o)
        while (added && size > limit) {
            super.remove()
        }
        return added
    }
}

package com.velocidi

import java.util.*

class FixedSizeQueue<E>(private val limit: Int) : LinkedList<E>() {

    override fun add(o: E): Boolean {
        val added = super.add(o)
        while (added && size > limit) {
            super.remove()
        }
        return added
    }
}
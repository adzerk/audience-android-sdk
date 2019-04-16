package com.velocidi

import android.content.Context
import java.util.*

abstract class FetchAdvertisingId<T>(context: Context) {

    lateinit var adInfo: AdvertisingInfo

    internal val queue: Queue<T> = FixedSizeQueue(300)

    init {
        fetchAndSetAdvertisingId(context)
    }

    open fun fetchAndSetAdvertisingId(context: Context) {

        val listener = object : AdvertisingIdListener {
            override fun fetchAdvertisingIdCompleted(advertisingInfo: AdvertisingInfo) {
                this@FetchAdvertisingId.adInfo = advertisingInfo

                onFetchCompleted()

                while (queue.size != 0) {
                    handleTask(queue.remove())
                }
            }
        }
        GetAdvertisingIdTask(listener).execute(context)
    }

    fun runTask(task: T) {
        if (::adInfo.isInitialized) handleTask(task) else queue.add(task)
    }
    abstract fun handleTask(task: T)

    abstract fun onFetchCompleted()
}

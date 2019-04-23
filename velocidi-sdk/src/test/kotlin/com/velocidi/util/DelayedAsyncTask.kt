package com.velocidi.util

import android.os.AsyncTask

internal class DelayedAsyncTask(val listener: () -> Unit) : AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg params: Unit?) {
        getAdvertisingId()
    }

    fun getAdvertisingId() {
        Thread.sleep(2000)
        return
    }

    override fun onPostExecute(result: Unit?) {
        listener()
    }
}

package com.dading.ssqs.utils

import android.util.Log


object Logger {

    fun d(TAG: String, msg: String) {
        Log.d(TAG, msg)
    }

    fun e(TAG: String, mes: String) {
        Log.e(TAG, mes)
    }

    fun e(tag: String, message: String, exception: Throwable) {
        Log.e(tag, message, exception)
    }

    fun e(tag: String, e: Throwable) {
        e.printStackTrace()
        Log.e(tag, e.message, e)
    }
}

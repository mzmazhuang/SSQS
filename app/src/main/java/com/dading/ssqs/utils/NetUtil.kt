package com.dading.ssqs.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Created by mazhuang on 2017/11/20.
 * 网络相关的工具类
 */

object NetUtil {
    /**
     * Get the network user
     *
     * @param context
     * @return
     */

    private fun getNetworkInfo(context: Context?): NetworkInfo? {
        var ret: NetworkInfo? = null
        if (context != null) {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            try {
                ret = cm.activeNetworkInfo
            } catch (ignored: Throwable) {
            }

        }
        return ret
    }


    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */

    fun isConnected(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected
    }


    fun isAvailable(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isAvailable
    }


    /**
     * Check if there is any connectivity to a Wifi network
     *
     * @param context
     * @return
     */

    fun isConnectedWifi(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
    }


    /**
     * Check if there is any connectivity to a mobile network
     *
     * @param context
     * @return
     */

    fun isConnectedMobile(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_MOBILE
    }


    fun getNetworkTypeName(context: Context): String {
        var type = ""
        val info = getNetworkInfo(context)
        if (info != null && info.isConnected) {
            type = info.typeName
        }
        return type
    }
}

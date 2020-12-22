package com.badmitry.futurespositions.ui.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.badmitry.futurespositions.mvp.model.network.INetworkChecker
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

class AndroidNetworkChecker(private val context: Context): INetworkChecker {
    override fun isConnect(): Single<Boolean> = Single.create<Boolean>{ emitter ->
        val cm: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = cm.activeNetwork
            nw?.let{
                val actNw = cm.getNetworkCapabilities(nw)
                actNw?.let{
                    when {
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> emitter.onSuccess(isInternetAvailable())
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> emitter.onSuccess(isInternetAvailable())
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> emitter.onSuccess(isInternetAvailable())
                        else -> emitter.onSuccess(false)
                    }
                } ?: emitter.onSuccess(false)
            } ?: emitter.onSuccess(false)
        } else {
            val nwInfo = cm.activeNetworkInfo
            nwInfo?.let {emitter.onSuccess(nwInfo.isConnected)}?: emitter.onSuccess(false)
        }
    }.subscribeOn(Schedulers.io())

    private fun isInternetAvailable(): Boolean {
        try {
            val sock = Socket()
            val sockaddr: SocketAddress = InetSocketAddress("8.8.8.8", 53)
            sock.connect(sockaddr, 10000)
            sock.close()
            return true
        } catch (e: IOException) {
            return false
        }
    }
}
package com.openclassrooms.realestatemanager.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Clock
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt


class Utils @Inject constructor(
    private val clock: Clock,
    @ApplicationContext private val context: Context,
) {

    companion object {

        @RequiresApi(Build.VERSION_CODES.O)
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    }

    /**
     * @param dollars
     * @return
     */
    fun convertDollarToEuro(dollars: Int): Int {
        return (dollars * 0.93).roundToInt()
    }


    fun convertEuroToDollar(euro: Int): Int {
        return (euro * 1.07).roundToInt()
    }

    /**
     * @return
     */
    val todayDate: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            return LocalDate.now(clock).format(dateFormat)
        }

    /**
     * @return
     */

    @SuppressLint("MissingPermission")
    fun isNetWorkConnected() : Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return manager.getNetworkCapabilities(manager.activeNetwork)?.let {
            it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            it.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ||
            it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
            it.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        } ?: false

    }


}
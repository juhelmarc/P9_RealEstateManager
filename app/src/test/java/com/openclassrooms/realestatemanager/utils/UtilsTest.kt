package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

import org.robolectric.shadows.ShadowNetworkCapabilities
import java.time.*


@RunWith(RobolectricTestRunner::class)
class UtilsTest {

    private lateinit var utils : Utils

    companion object {
        private const val dollarToEuro = 100

        private const val euroToDollar = 100
    }
    private var connectivityManager = getApplicationContext<Context>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Before
    fun setUp() {
         utils  = Utils(
         Clock.fixed(
             LocalDate.of(2023, 2, 16).atStartOfDay().toInstant(ZoneOffset.UTC),
             ZoneOffset.UTC
         ),
             RuntimeEnvironment.getApplication())
    }

    @Test
    fun convertDollarToEuroSuccess() {
        val expectedValue = 93

        val euroValue = utils.convertDollarToEuro(dollarToEuro)

        assertEquals(expectedValue, euroValue)
    }

    @Test
    fun convertEuroToDollarSuccess() {
        val expectedValue = 107

        val dollarValue = utils.convertEuroToDollar(euroToDollar)

        assertEquals(expectedValue, dollarValue)
    }

    @Test
    fun getTodayDateSuccess() {
        val expectedDate = "16/02/2023"

        val toDayDate = utils.todayDate

        assertEquals(expectedDate, toDayDate)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.P])
    fun isNetWorkConnectedSuccess() {
        val networkCapabilities = ShadowNetworkCapabilities.newInstance()
        shadowOf(networkCapabilities).addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        shadowOf(connectivityManager).setNetworkCapabilities(connectivityManager.activeNetwork, networkCapabilities)

        assertTrue(utils.isNetWorkConnected())
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.P])
    fun isNetWorkConnectedFailure() {
        val networkCapabilities = ShadowNetworkCapabilities.newInstance()
        shadowOf(connectivityManager).setNetworkCapabilities(connectivityManager.activeNetwork, networkCapabilities)

        assertFalse(utils.isNetWorkConnected())
    }
}
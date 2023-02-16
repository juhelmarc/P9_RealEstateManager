package com.openclassrooms.realestatemanager.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.ui.detail.DetailActivity
import com.openclassrooms.realestatemanager.ui.detail.DetailFragment
import com.openclassrooms.realestatemanager.ui.filter.FilterActivity
import com.openclassrooms.realestatemanager.ui.formProperty.FormPropertyActivity
import com.openclassrooms.realestatemanager.ui.list.ListFragment
import com.openclassrooms.realestatemanager.ui.map.MapsActivity
import com.openclassrooms.realestatemanager.ui.simulator.LoanSimulatorActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.setCurrentPropertyId(0L)
        createNotificationChannel()
        if (saveInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.containerList.id, ListFragment())
                .commitNow()
        }
        if (binding.containerDetail != null && supportFragmentManager.findFragmentById(binding.containerDetail.id) == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    binding.containerDetail.id,
                    DetailFragment()
                )
                .commitNow()
        }
        viewModel.navigateSingleLiveEvent.observe(this) {
            when (it) {
                MainViewAction.NavigateToDetailActivity -> startActivity(
                    Intent(
                        this,
                        DetailActivity::class.java
                    )
                )
            }
        }

        val menuProviderEmptyList: MenuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu_empty_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {
                R.id.borrow -> {
                    startActivity(Intent(this@MainActivity, LoanSimulatorActivity::class.java))
                    true
                }
                R.id.map -> {
                    startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                    true
                }
                R.id.add_property -> {
                    viewModel.setIsAnUpdateProperty(false)
                    startActivity(Intent(this@MainActivity, FormPropertyActivity::class.java))
                    true
                }

                R.id.search -> {
                    startActivity(Intent(this@MainActivity, FilterActivity::class.java))
                    true
                }

                R.id.search_off -> {
                    viewModel.deleteCurrentFilter()
                    true
                }
                else -> false
            }
        }

        val menuProvider: MenuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {
                R.id.borrow -> {
                    startActivity(Intent(this@MainActivity, LoanSimulatorActivity::class.java))
                    true
                }
                R.id.map -> {
                    startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                    true
                }
                R.id.add_property -> {
                    viewModel.setIsAnUpdateProperty(false)
                    startActivity(Intent(this@MainActivity, FormPropertyActivity::class.java))
                    true
                }
                R.id.edit_current_property -> {
                    viewModel.setIsAnUpdateProperty(true)
                    startActivity(Intent(this@MainActivity, FormPropertyActivity::class.java))
                    true
                }

                R.id.search -> {
                    startActivity(Intent(this@MainActivity, FilterActivity::class.java))
                    true
                }

                R.id.search_off -> {
                    viewModel.deleteCurrentFilter()
                    true
                }
                else -> false
            }
        }

        viewModel.getListPropertyLiveData().observe(this) { listProperty ->
            removeMenuProvider(menuProvider)
            removeMenuProvider(menuProviderEmptyList)
            if (listProperty.isEmpty()) {
                removeMenuProvider(menuProvider)
                addMenuProvider(menuProviderEmptyList)
            } else {
                removeMenuProvider(menuProviderEmptyList)
                addMenuProvider(menuProvider)
            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "getString(R.string.channel_name)"
            val descriptionText = "getString(R.string.channel_description)"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onConfigurationChanged(resources.getBoolean(R.bool.isTablet))

    }

    companion object {
        private const val CHANNEL_ID = "ChannelId"
    }

}
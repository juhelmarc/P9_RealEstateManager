package com.openclassrooms.realestatemanager.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.ui.addProperty.AddOrUpdatePropertyActivity
import com.openclassrooms.realestatemanager.ui.detail.DetailActivity
import com.openclassrooms.realestatemanager.ui.detail.DetailFragment
import com.openclassrooms.realestatemanager.ui.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private val ISADDACTIVITY = "0"
    private val ISUPDATEACTIVITY = "1"

    override fun onCreate(saveInstanceState : Bundle?) {
        super.onCreate(saveInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Si l'activité n'est pas en pause -> ajout du listFragment dans le frameLayout
        if(saveInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(binding.containerList.id, ListFragment())
                    .commitNow()

        }
        // Si la vue du frameLayout containerDetail est différent de null donc visible c'est à dire que l'appareil est en mode land ou que c'est une tablet
        // Et si le fragment DetailFragment est égale à null donc n'est pas présent dans la main activité ->
        // on l'ajoute dans le frameLayout containerDetail
        if(binding.containerDetail != null && supportFragmentManager.findFragmentById(binding.containerDetail.id) == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    binding.containerDetail.id,
                    DetailFragment())
                .commitNow()
        }
        viewModel.navigateSingleLiveEvent.observe(this) {
            when(it) {
                //-> = alors je fais
                MainViewAction.NavigateToDetailActivity -> startActivity(Intent(this, DetailActivity::class.java))
            }
        }

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {
                R.id.add_property -> {
                    startActivity(Intent(this@MainActivity, AddOrUpdatePropertyActivity::class.java).putExtra("AddOrUpdate", ISADDACTIVITY))
                    true
                }
                R.id.search -> {

                    true
                }
                R.id.edit_current_property -> {
                    startActivity(Intent(this@MainActivity, AddOrUpdatePropertyActivity::class.java).putExtra("AddOrUpdate", ISUPDATEACTIVITY))
                    true
                }
                else -> false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.onConfigurationChanged(resources.getBoolean(R.bool.isTablet))
    }

}
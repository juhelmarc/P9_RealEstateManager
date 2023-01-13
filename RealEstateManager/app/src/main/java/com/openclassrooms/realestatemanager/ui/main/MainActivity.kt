package com.openclassrooms.realestatemanager.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.ui.formProperty.FormPropertyActivity
import com.openclassrooms.realestatemanager.ui.detail.DetailActivity
import com.openclassrooms.realestatemanager.ui.detail.DetailFragment
import com.openclassrooms.realestatemanager.ui.filter.FilterActivity
import com.openclassrooms.realestatemanager.ui.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(saveInstanceState : Bundle?) {
        super.onCreate(saveInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Ouvrir une nouvelle activité, Startavctivityforresult,
        //créer un model avec les filtres, la vue peux retourner une custom request (string) (sur room définir des custom request)
        //qui retourne un résultat et la vue mainActivité récupère la custom request la lance dans room
        //SupportSQLiteQuery
        viewModel.setQueryFilter("", false)
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
        val startForFilterActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if(result.resultCode == RESULT_OK) {
                    val data = result.data!!.getStringExtra("data")
                    if(data != null) {
                        viewModel.setQueryFilter(data, true)
                    }
                }
            }
        viewModel.currentIdLiveData.observe(this) {
            if(it == 0L || it == null) {

            }
        }
        //Menu provider, add menu provider et Remove menu provider en fonction de la valeur de currentIdLiveData remove le menu et ajouter le nouveau
        //ajouter un filtre, permettant de ne pas reproduire la même chose si la valeur est la même
        addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {

                R.id.add_property -> {
                viewModel.setPropertyIdAdd()
                startActivity(Intent(this@MainActivity, FormPropertyActivity::class.java))
                true
                }
                R.id.search -> {
                startForFilterActivityResult.launch(Intent(this@MainActivity, FilterActivity()::class.java))
                true
                }
                //TODO:Observer si une propriété est selectionné si c'est le cas afficher le bouton
                R.id.edit_current_property -> {
                startActivity(Intent(this@MainActivity, FormPropertyActivity::class.java))
                true
                }
                R.id.search_off -> {
                viewModel.setQueryFilter("", false)
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
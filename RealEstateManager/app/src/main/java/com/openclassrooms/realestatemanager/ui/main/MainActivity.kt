package com.openclassrooms.realestatemanager.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.ui.detail.DetailActivity
import com.openclassrooms.realestatemanager.ui.detail.DetailFragment
import com.openclassrooms.realestatemanager.ui.detailslider.DetailSliderFragment
import com.openclassrooms.realestatemanager.ui.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

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
    }

    override fun onResume() {
        super.onResume()

        viewModel.onConfigurationChanged(resources.getBoolean(R.bool.isTablet))
    }




}
package com.openclassrooms.realestatemanager.ui.addProperty

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.openclassrooms.realestatemanager.databinding.ActivityAddOrUpdatePropertyBinding
import com.openclassrooms.realestatemanager.ui.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddOrUpdatePropertyActivity: AppCompatActivity() {

    private val viewModel by viewModels<AddOrUpdatePropertyViewModel>()

    private val ISADDACTIVITY: String = "0"
    private val ISUPDATEACTIVITY: String = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent: Intent = intent
        val binding = ActivityAddOrUpdatePropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            if (intent.getStringExtra("AddOrUpdate") == ISADDACTIVITY) {
                supportFragmentManager.beginTransaction()
                    .replace(binding.containerAddProperty.id, AddPropertyFragment())
                    .commitNow()
            } else if (intent.getStringExtra("AddOrUpdate") == ISUPDATEACTIVITY) {
                supportFragmentManager.beginTransaction()
                    .replace(binding.containerAddProperty.id, UpdatePropertyFragment())
                    .commitNow()
            }
        }

    }


}
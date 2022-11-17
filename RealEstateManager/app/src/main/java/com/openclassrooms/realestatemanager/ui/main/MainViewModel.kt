package com.openclassrooms.realestatemanager.ui.main

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.utils.SingleLiveEvent
import com.openclassrooms.realestatemanager.data.current_property.CurrentPropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
     currentPropertyRepository: CurrentPropertyRepository
) : ViewModel() {

    private var isTablet: Boolean = false


    val navigateSingleLiveEvent = SingleLiveEvent<MainViewAction>()


    init {
        navigateSingleLiveEvent.addSource(currentPropertyRepository.currentIdLiveData) {
            if(!isTablet) {
                navigateSingleLiveEvent.setValue(MainViewAction.NavigateToDetailActivity)
            }
        }
    }

    //dans la onResume méthode de la MainActivity nous appellons cette méthode et on verrifie
    fun onConfigurationChanged(isTablet: Boolean) {
        this.isTablet = isTablet
    }




}
package com.openclassrooms.realestatemanager.ui.main

import androidx.lifecycle.*

import com.openclassrooms.realestatemanager.utils.SingleLiveEvent
import com.openclassrooms.realestatemanager.data.repositories.CurrentPropertyRepository
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currentPropertyRepository: CurrentPropertyRepository,
    private val propertyRepository: PropertyRepository,
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

    fun setIsAnUpdateProperty(isAnUpdate: Boolean) {
        propertyRepository.setIsAnUpdateProperty(isAnUpdate)
    }

    fun deleteCurrentFilter() {
        propertyRepository.deleteCurrentFilter()
    }

    val currentIdLiveData: LiveData<Long> =
        currentPropertyRepository.currentIdLiveData

    fun setCurrentPropertyId(id: Long) {
        propertyRepository.setCurrentPropertyId(id)
    }

}
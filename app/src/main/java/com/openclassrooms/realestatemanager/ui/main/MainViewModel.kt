package com.openclassrooms.realestatemanager.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import com.openclassrooms.realestatemanager.data.repositories.CurrentPropertyRepository
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import com.openclassrooms.realestatemanager.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currentPropertyRepository: CurrentPropertyRepository,
    private val propertyRepository: PropertyRepository,
) : ViewModel() {

    private var isTablet: Boolean = false

    val navigateSingleLiveEvent = SingleLiveEvent<MainViewAction>()


    fun onConfigurationChanged(isTablet: Boolean) {
        this.isTablet = isTablet
    }

    fun getListPropertyLiveData(): LiveData<List<PropertyEntity>> {
       return propertyRepository.getAllProperty().map { listProperty ->
            listProperty
        }.asLiveData()
    }

    fun setIsAnUpdateProperty(isAnUpdate: Boolean) {
        propertyRepository.setIsAnUpdateProperty(isAnUpdate)
    }

    fun deleteCurrentFilter() {
        propertyRepository.deleteCurrentFilter()
    }

    fun setCurrentPropertyId(id: Long) {
        propertyRepository.setCurrentPropertyId(id)
    }

    init {
        navigateSingleLiveEvent.addSource(currentPropertyRepository.currentIdLiveData) {
            if (!isTablet) {
                navigateSingleLiveEvent.setValue(MainViewAction.NavigateToDetailActivity)
            }
        }
    }

}
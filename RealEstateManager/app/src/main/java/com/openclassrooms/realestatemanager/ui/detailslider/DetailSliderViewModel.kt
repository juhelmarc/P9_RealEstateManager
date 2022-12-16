package com.openclassrooms.realestatemanager.ui.detailslider

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.data.repositories.CurrentPropertyRepository
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class DetailSliderViewModel @Inject constructor(
    propertyRepository: PropertyRepository,
    currentPropertyRepository: CurrentPropertyRepository
) : ViewModel(){

    var detailLiveData : LiveData<DetailSliderViewState> =
        currentPropertyRepository.currentIdLiveData.switchMap { id ->
            propertyRepository.getAllPicturesOfThisProperty(id).map {
                 DetailSliderViewState(it)
            }.asLiveData()
        }

    init {
        if(currentPropertyRepository.currentIdLiveData.value == null) {
            detailLiveData = propertyRepository.getAllPicturesOfThisProperty(1).map {
                DetailSliderViewState(it)
            }.asLiveData()
        }
    }


}
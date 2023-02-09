package com.openclassrooms.realestatemanager.ui.detailslider

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.openclassrooms.realestatemanager.data.repositories.CurrentPropertyRepository
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class DetailSliderViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val currentPropertyRepository: CurrentPropertyRepository
) : ViewModel() {

    fun getDetailSlider() : LiveData<DetailSliderViewState> {
        val detailSliderLiveData: LiveData<DetailSliderViewState> =
            if (currentPropertyRepository.currentIdLiveData.value != null) {
                currentPropertyRepository.currentIdLiveData.switchMap { id ->
                    propertyRepository.getAllPicturesOfThisProperty(id).map {
                        DetailSliderViewState(it)
                    }.asLiveData()
                }
            } else {
                propertyRepository.getAllPicturesOfThisProperty(1).map {
                    DetailSliderViewState(it)
                }.asLiveData()
            }
        return detailSliderLiveData
    }



}
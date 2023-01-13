package com.openclassrooms.realestatemanager.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.openclassrooms.realestatemanager.data.PoiList
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.switchMap
import javax.inject.Inject

@HiltViewModel
class FilterViewModel  @Inject constructor(
    private val propertyRepository: PropertyRepository
) : ViewModel() {

    val filterViewStateLiveData: LiveData<FilterViewState> =
        propertyRepository.getListIdProperty().switchMap {
            combine(
                propertyRepository.getMinMaxPriceAndSurface(it),
                propertyRepository.getAllAgent(),
                propertyRepository.getListType(),
                propertyRepository.getListTown(),
            ) {  minMaxPriceSurface, listAgent, listType, listTown ->
                FilterViewState(
                    minPrice = minMaxPriceSurface.minPrice,
                    maxPrice = minMaxPriceSurface.maxPrice,
                    minSurface = minMaxPriceSurface.minSurface,
                    maxSurface = minMaxPriceSurface.maxSurface,
                    listAgent = listAgent,
                    listOfType = listType,
                    listOfTown = listTown,
                )
            }.asLiveData()
        }



}
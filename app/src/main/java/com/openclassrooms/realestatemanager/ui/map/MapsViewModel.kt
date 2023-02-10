package com.openclassrooms.realestatemanager.ui.map

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
class MapsViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val currentPropertyRepository: CurrentPropertyRepository
) : ViewModel() {

    fun getMapsViewStateListLiveData() : LiveData<List<MapsViewState>> {
       return propertyRepository.getQueryFilter().switchMap { query ->
            propertyRepository.getAllPropertyFilter(query).map { listFilterProperty ->
                listFilterProperty.map { property ->
                    MapsViewState(
                        id = property.id,
                        lat = property.lat,
                        lng = property.lng,
                        price = property.price,
                        picture = property.mainPicture,
                        town = property.town,
                        type = property.type
                    )
                }
            }.asLiveData()
        }
    }

    fun onItemMarkerClick(id: Long) {
        currentPropertyRepository.setCurrentId(id)
        propertyRepository.setCurrentPropertyId(id)
    }
}
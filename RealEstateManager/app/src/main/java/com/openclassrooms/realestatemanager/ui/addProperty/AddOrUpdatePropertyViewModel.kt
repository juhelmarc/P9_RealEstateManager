package com.openclassrooms.realestatemanager.ui.addProperty

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddOrUpdatePropertyViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository
): ViewModel() {



    fun addProperty(property: PropertyEntity) = viewModelScope.launch {
        propertyRepository.insertProperty(property)
    }

    fun getAllAgent(): LiveData<List<AgentEntity>> {
      return propertyRepository.getAllAgent().asLiveData()
    }





}
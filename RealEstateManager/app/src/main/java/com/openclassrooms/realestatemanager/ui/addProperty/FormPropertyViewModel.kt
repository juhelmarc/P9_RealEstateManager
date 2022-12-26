package com.openclassrooms.realestatemanager.ui.addProperty

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import com.openclassrooms.realestatemanager.data.repositories.FormPropertyRepository
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormPropertyViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val formPropertyRepository: FormPropertyRepository
): ViewModel() {

    private val messageError = "Is mandatory"

    fun addProperty(property: PropertyEntity) = viewModelScope.launch {
        propertyRepository.insertProperty(property)
    }

    fun getAllAgent(): LiveData<List<AgentEntity>> {
      return propertyRepository.getAllAgent().asLiveData()
    }


    private val formPropertyEmpty =
        PropertyEntity(
            agentId = 0L,
            agentName = "",
            type = "",
            district = "",
            price = "",
            description = "",
            surface = "",
            numberOfRooms = "",
            numberOfBathrooms = "",
            numberOfBedrooms = "",
            town = "",
            address = "",
            postalCode = "",
            state = "",
            mainPicture = null,
            isAvailable = true,
            entryDate = "",
            dateOfSale = "",
        )
    private val propertyMutableStateFlow = MutableStateFlow(formPropertyEmpty)

    init {
        if(propertyRepository.formPropertyIdFlow == 0L) {
            propertyMutableStateFlow.value = formPropertyEmpty
        } else {
              getPropertyByIdFlow(propertyRepository.formPropertyIdFlow).map { propertyToUpdate ->
                propertyMutableStateFlow.value = propertyToUpdate
            }
        }
    }

    private fun getPropertyFlow(): Flow<PropertyEntity> {
        return propertyMutableStateFlow
    }

    private fun getPropertyByIdFlow(id: Long): Flow<PropertyEntity> {
        return propertyRepository.getPropertyByIdFlow(id)
    }

    val formPropertyViewStateLiveData: LiveData<FormPropertyViewState> = combine(
        propertyRepository.getAllAgent(),
        getPropertyFlow(),
        formPropertyRepository.formErrorMutableStateFlow,
    //TODO : dégager le formRepository, récupérer l'id pour la PropertyEntity récupérer les données du formPropertyRepo et les mettre dans le viewmodel
    ) { agentList, formProperty, error ->
        FormPropertyViewState(
            id = formProperty.id,
            photoList = null,
            agentList = agentList,
            agentId = formProperty.agentId,
            agentName = formProperty.agentName,
            type = formProperty.type,
            district = formProperty.district,
            price = formProperty.price,
            description = formProperty.description,
            surface = formProperty.surface,
            numberOfRooms = formProperty.numberOfRooms,
            numberOfBathrooms = formProperty.numberOfBathrooms,
            numberOfBedrooms = formProperty.numberOfBedrooms,
            town = formProperty.town,
            address = formProperty.address,
            postalCode = formProperty.postalCode,
            state = formProperty.state,
            mainPicture = formProperty.mainPicture,
            isAvailable = formProperty.isAvailable,
            entryDate = formProperty.entryDate,
            dateOfSale = formProperty.dateOfSale,

            agentError = error.agentError,
            typeError = error.typeError,
            postalCodeError = error.postalCodeError,
            districtError = error.districtError,
            addressError = error.addressError,
            townError = error.townError,
            entryDateError = error.entryDateError,
            dateOfSaleError = error.dateOfSaleError
        )
    }.asLiveData()


    private fun getPropertyToUpdate(): PropertyEntity {
        return propertyMutableStateFlow.value
    }

     fun onSubmitButtonClicked(): LiveData<Boolean> {
         if(checkError()) {
             updatePropertyEntity(formPropertyRepository.getForm())
             formHasNoError.value = true
        } else {
            formHasNoError.value = false
         }
         return formHasNoError
    }





    private val formHasNoError = MutableLiveData<Boolean>(false)

    private fun checkError(): Boolean {
        if(formPropertyRepository.getForm().agentName == "") {
           formPropertyRepository.setError(
               formPropertyRepository.getFormError().copy(agentError = "Is mandatory"))
            return true //Guard de swift
        } else {
           formPropertyRepository.setError(
               formPropertyRepository.getFormError().copy(agentError = null))
        }
        if(formPropertyRepository.getForm().type == "") {
           formPropertyRepository.setError(
               formPropertyRepository.getFormError().copy(typeError = "Is mandatory"))
        } else {
           formPropertyRepository.setError(
               formPropertyRepository.getFormError().copy(typeError = null))
        }

        if(formPropertyRepository.getForm().postalCode.toString().length != 5) {
            formPropertyRepository.setError(
                formPropertyRepository.getFormError().copy(postalCodeError = "5 numbers are required")
            )
        } else {
            formPropertyRepository.setError(
                formPropertyRepository.getFormError().copy(postalCodeError = null)
            )
        }
        if(formPropertyRepository.getForm().district == "") {
            formPropertyRepository.setError(
                formPropertyRepository.getFormError().copy(districtError = "Is mandatory")
            )
        } else {
            formPropertyRepository.setError(
                formPropertyRepository.getFormError().copy(districtError = null)
            )
        }
        if(formPropertyRepository.getForm().address == "") {
            formPropertyRepository.setError(
                formPropertyRepository.getFormError().copy(addressError = "Is mandatory")
            )
        } else {
            formPropertyRepository.setError(
                formPropertyRepository.getFormError().copy(addressError = null)
            )
        }
        if(formPropertyRepository.getForm().town == "") {
            formPropertyRepository.setError(
                formPropertyRepository.getFormError().copy(townError = "Is mandatory")
            )
        } else {
            formPropertyRepository.setError(
                formPropertyRepository.getFormError().copy(townError = null)
            )
        }
        if(formPropertyRepository.getForm().entryDate == null && formPropertyRepository.getForm().dateOfSale != null) {
            formPropertyRepository.setError(
                formPropertyRepository.getFormError().copy(entryDateError = "Edit entry date")
            )
        } else {
            formPropertyRepository.setError(
                formPropertyRepository.getFormError().copy(entryDateError = null)
            )
        }
        if( formPropertyRepository.getForm().entryDate != null && formPropertyRepository.getForm().dateOfSale != null &&
            formPropertyRepository.getForm().entryDate!! > formPropertyRepository.getForm().dateOfSale!!
        ) {
            formPropertyRepository.setError(
                formPropertyRepository.getFormError().copy(dateOfSaleError = "Date of sale should be after entry date")
            )
        } else {
            formPropertyRepository.setError(
                formPropertyRepository.getFormError().copy(dateOfSaleError = null)
            )
        }
        return formPropertyRepository.getFormError().agentError == null &&
                formPropertyRepository.getFormError().typeError == null &&
                formPropertyRepository.getFormError().postalCodeError == null &&
                formPropertyRepository.getFormError().districtError == null &&
                formPropertyRepository.getFormError().addressError == null &&
                formPropertyRepository.getFormError().townError == null &&
                formPropertyRepository.getFormError().entryDateError == null &&
                formPropertyRepository.getFormError().dateOfSaleError == null
       }


    private fun updatePropertyEntity(propertyEntity: PropertyEntity) = viewModelScope.launch {
        propertyRepository.insertProperty(propertyEntity)
    }

    fun updateType(typeToUpdate: String?) {
       propertyMutableStateFlow.value = getPropertyToUpdate().copy(type = typeToUpdate)
    }
//    fun livedataPropertyEntity = LiveData<PropertyEntity> = propertyRepository.getPropertyByIdFlow()
    // livedataPropertyEntity.copy()
    fun updateAgent(agent: AgentEntity) {
        formPropertyRepository.setFormUpdate(
            formPropertyRepository.getForm().copy(
                agentId = agent.agentId,
                agentName = agent.name
            )
        )
    }
    fun updateSurface(surface: String?) {
        formPropertyRepository.setFormUpdate(
            formPropertyRepository.getForm().copy(
                surface = surface
            )
        )
    }
    fun updateRoom(nbrRoom: String?) {
        formPropertyRepository.setFormUpdate(
            formPropertyRepository.getForm().copy(
                numberOfRooms = nbrRoom
            )
        )
    }
    fun updateBathRoom(nbrBathRoom: String?) {
        formPropertyRepository.setFormUpdate(
            formPropertyRepository.getForm().copy(
                numberOfBathrooms = nbrBathRoom
            )
        )
    }
    fun updateBedRoom(nbrBedRoom: String?) {
        formPropertyRepository.setFormUpdate(
            formPropertyRepository.getForm().copy(
                numberOfBedrooms = nbrBedRoom
            )
        )
    }
    fun updatePostalCode(postalCode: String?) {
        formPropertyRepository.setFormUpdate(
            formPropertyRepository.getForm().copy(
                postalCode = postalCode
            )
        )
    }
    fun updateDistrict(district: String?) {
        formPropertyRepository.setFormUpdate(
            formPropertyRepository.getForm().copy(
                district = district
            )
        )
    }
    fun updateAddress(address: String?) {
        formPropertyRepository.setFormUpdate(
            formPropertyRepository.getForm().copy(
                address = address)
        )
    }
    fun updateState(state: String?) {
        formPropertyRepository.setFormUpdate(
            formPropertyRepository.getForm().copy(
                state = state
            )
        )
    }
    fun updateEntryDate(entryDate: String?) {
        formPropertyRepository.setFormUpdate(
            formPropertyRepository.getForm().copy(
                entryDate = entryDate
            )
        )
    }
    fun updateDateOfSale(dateOfSale: String?) {
        formPropertyRepository.setFormUpdate(
            formPropertyRepository.getForm().copy(
                dateOfSale = dateOfSale
            )
        )
    }
    fun updatePrice(price: String?) {
        formPropertyRepository.setFormUpdate(
            formPropertyRepository.getForm().copy(
                price = price
            )
        )
    }
    fun updateTown(town: String?) {
        formPropertyRepository.setFormUpdate(
            formPropertyRepository.getForm().copy(
                town = town
            )
        )
    }




}
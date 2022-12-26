package com.openclassrooms.realestatemanager.data.repositories

import com.openclassrooms.realestatemanager.data.model.FormError
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FormPropertyRepository @Inject constructor() {

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

    private val formError: FormError = FormError(
        agentError = null,
        typeError = null,
        postalCodeError = null,
        districtError = null,
        addressError = null,
        townError = null,
        entryDateError = null,
        dateOfSaleError = null
    )

    private val formPropertyMutableStateFlow = MutableStateFlow(formPropertyEmpty)

//    var formPropertyFlow = formPropertyMutableStateFlow

//    private var formProperty: PropertyEntity = formPropertyEmpty

    fun getFormPropertyFlow(): Flow<PropertyEntity> {
       return formPropertyMutableStateFlow
    }

    fun getForm(): PropertyEntity {
        return formPropertyMutableStateFlow.value
    }

    fun setFormAdd() {
        formPropertyMutableStateFlow.value = formPropertyEmpty
//        formProperty = formPropertyEmpty
    }

    fun setFormUpdate(formToUpdate: PropertyEntity) {
        formPropertyMutableStateFlow.value = formToUpdate
    }

//    fun setFormProperty(propertyEntity: PropertyEntity) {
//        formProperty = propertyEntity
//    }



    val formErrorMutableStateFlow = MutableStateFlow(formError)

    fun setError(formError: FormError) {
        formErrorMutableStateFlow.value = formError
    }

    fun getFormError(): FormError {
        return formErrorMutableStateFlow.value
    }

   fun resetFormProperty() {
//       formProperty = formPropertyEmpty
       formPropertyMutableStateFlow.value = formPropertyEmpty
   }

}
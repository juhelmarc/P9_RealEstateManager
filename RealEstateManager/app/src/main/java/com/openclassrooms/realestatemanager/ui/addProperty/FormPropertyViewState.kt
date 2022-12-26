package com.openclassrooms.realestatemanager.ui.addProperty

import com.openclassrooms.realestatemanager.data.model.FormError
import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyPicturesEntity

data class FormPropertyViewState(
    val id: Long,
    val photoList: List<PropertyPicturesEntity>?,
    val agentList: List<AgentEntity?>,
    val agentId: Long?,
    val agentName: String?,
    val type: String?,
    val district: String?,
    val price: String?,
    val description: String?,
    val surface: String?,
    val numberOfRooms: String?,
    val numberOfBathrooms: String?,
    val numberOfBedrooms: String?,
    val town: String?,
    var address: String?,
    val postalCode: String?,
    val state: String?,
    val mainPicture: String?,
    val isAvailable: Boolean,
    val entryDate: String?,
    val dateOfSale: String?,

    val typeError: String?,
    val agentError: String?,
    val postalCodeError: String?,
    val districtError: String?,
    val addressError: String?,
    val townError: String?,
    val entryDateError: String?,
    val dateOfSaleError: String?

)

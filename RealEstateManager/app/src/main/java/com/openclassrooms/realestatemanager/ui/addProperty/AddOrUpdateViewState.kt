package com.openclassrooms.realestatemanager.ui.addProperty

import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyPicturesEntity

data class AddOrUpdateViewState(
    val photoList: List<PropertyPicturesEntity>?,
    val agent: AgentEntity?,
    val type: String?,
    val district: String?,
    val price: String?,
    val description: String?,
    val surface: Int?,
    val numberOfRooms: Int?,
    val numberOfBathrooms: Int?,
    val numberOfBedrooms: Int?,
    val town: String?,
    var address: String?,
    val postalCode: Int?,
    val state: String?,
    val mainPicture: String?,
    val isAvailable: Boolean,
    val entryDate: Long?,
    val dateOfSale: Long?,
)

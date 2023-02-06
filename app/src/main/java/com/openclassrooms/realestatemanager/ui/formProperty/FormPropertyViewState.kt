package com.openclassrooms.realestatemanager.ui.formProperty

import com.openclassrooms.realestatemanager.data.models.entities.PropertyPictureEntity


data class FormPropertyViewState(
    val id: Long,
    val listPicture: List<PropertyPictureEntity>,
    val agentId: Long,
    val agentName: String,
    val type: String?,
    val price: Int?,
    val description: String?,
    val surface: Int?,
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
    val listPoiSelectedOrNot: List<ChipPoiViewState>,
    val lat: Double?,
    val lng: Double?,

    val typeError: String?,
    val agentError: String?,
    val postalCodeError: String?,
    val addressError: String?,
    val townError: String?,
    val entryDateError: String?,
    val dateOfSaleError: String?,
    val pictureError: String?,
    val latLngError: String?
) {
    data class ChipPoiViewState(
        val poiId: Int, val isSelected: Boolean
    )
}



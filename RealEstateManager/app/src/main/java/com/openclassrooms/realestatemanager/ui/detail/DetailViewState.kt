package com.openclassrooms.realestatemanager.ui.detail

import com.openclassrooms.realestatemanager.data.models.entities.PropertyPicturesEntity

sealed class DetailViewState() {

    data class Selected (
        val id : Long,
        val district : String?,
        val description: String?,
        val town: String?,
        val address: String?,
        val postalCode: Int?,
        val surface: Int?,
        val numberOfRooms: Int?,
        val numberOfBathrooms: Int?,
        val numberOfBedRooms: Int?,
        val state: String?,
    ) : DetailViewState()

    object Empty : DetailViewState()
}




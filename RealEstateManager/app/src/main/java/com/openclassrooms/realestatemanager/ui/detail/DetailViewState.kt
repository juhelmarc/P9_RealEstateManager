package com.openclassrooms.realestatemanager.ui.detail

import com.openclassrooms.realestatemanager.data.models.entities.PropertyPicturesEntity

sealed class DetailViewState() {

    data class Selected (
        val id : Long,
        val district : String?,
        val description: String?,
        val town: String?,
        val address: String?,
        val postalCode: String?,
        val surface: String?,
        val numberOfRooms: String?,
        val numberOfBathrooms: String?,
        val numberOfBedRooms: String?,
        val state: String?,
    ) : DetailViewState()

    object Empty : DetailViewState()
}




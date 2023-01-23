package com.openclassrooms.realestatemanager.ui.detail

import com.openclassrooms.realestatemanager.data.models.entities.PropertyPictureEntity

sealed class DetailViewState() {

    data class Selected (
        val id : Long,
        val description: String?,
        val town: String?,
        val address: String?,
        val postalCode: String?,
        val surface: Int?,
        val numberOfRooms: String?,
        val numberOfBathrooms: String?,
        val numberOfBedRooms: String?,
        val state: String?,
        val poiSelected: List<ChipPoiViewStateDetail>,
        val listPicture : List<PropertyPictureEntity>,
        val entryDate: String?,
        val sellingDate: String?
    ) : DetailViewState()

    object Empty : DetailViewState()
    data class  NotSelected(
        val id : Long,
        val description: String?,
        val town: String?,
        val address: String?,
        val postalCode: String?,
        val surface: Int?,
        val numberOfRooms: String?,
        val numberOfBathrooms: String?,
        val numberOfBedRooms: String?,
        val state: String?,
        val poiSelected: List<ChipPoiViewStateDetail>,
        val listPicture : List<PropertyPictureEntity>,
        val entryDate: String?,
        val sellingDate: String?
    ) : DetailViewState()
} data class ChipPoiViewStateDetail(
    val poiId: Int,
    val isSelected: Boolean
)




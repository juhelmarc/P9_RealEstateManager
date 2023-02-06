package com.openclassrooms.realestatemanager.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PropertyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var agentId: Long,
    var agentName: String,
    var type: String?,
    val price: Int?,
    val description: String?,
    val surface: Int?,
    val numberOfRooms: String?,
    val numberOfBathrooms: String?,
    val numberOfBedrooms: String?,
    val town: String?,
    val address: String?,
    val postalCode: String?,
    val state: String?,
    val mainPicture: String?,
    val isAvailable: Boolean,
    val entryDate: String?,
    val dateOfSale: String?,
    val poiSelected: List<Int>,
    val lat: Double?,
    val lng: Double?,
)
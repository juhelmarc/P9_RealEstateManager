package com.openclassrooms.realestatemanager.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PropertyPictureEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val propertyId: Long,
    val url: String,
    val title: String,
)




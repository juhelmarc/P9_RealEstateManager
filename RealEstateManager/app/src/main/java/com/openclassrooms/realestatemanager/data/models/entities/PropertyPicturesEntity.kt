package com.openclassrooms.realestatemanager.data.models.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = PropertyEntity::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("propertyId" ))])

data class PropertyPicturesEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val propertyId: Long,
    val url: String,
    val title: String,
)




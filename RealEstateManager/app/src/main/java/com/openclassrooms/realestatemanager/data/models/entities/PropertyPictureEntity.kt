package com.openclassrooms.realestatemanager.data.models.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity
//    (foreignKeys = [ForeignKey(entity = PropertyEntity::class,
//    parentColumns = arrayOf("id"),
//    childColumns = arrayOf("propertyId" ))])
//Cles intrangère cascade delete -> lorsque je supprime ma property, cela va supprimer toutes les images lié à la property @ForeignKey(onDelete = CASCADE)
data class PropertyPictureEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val propertyId: Long,
    val url: String,
    val title: String,
)




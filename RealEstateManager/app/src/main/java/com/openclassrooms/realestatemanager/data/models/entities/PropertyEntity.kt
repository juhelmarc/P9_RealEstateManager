package com.openclassrooms.realestatemanager.data.models.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

//Pour ajouter la relation clé-étrangère / clé-primaire à "la main"
//@Entity(foreignKeys = @ForeignKey(entity = Agent.class,
//        parentColumns = "id",
//        childColumns = "agentId"))

@Entity(foreignKeys = [ForeignKey(entity = AgentEntity::class,
    parentColumns = arrayOf("agentId"),
    childColumns = arrayOf("agentId" ))])
data class PropertyEntity(
    // pour inclure la classe Agent directement dans Property sans se soucier des clés étrangère.
    // Elle va ajouter automatiquement les champs de notre classe User dans Item et ainsi créer un relation
    // le prefix agent_ est obligatoire pour que ne nous ayons pas deux champs nommé id
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var agentId: Long,
    var agentName: String,
    var type: String?,
    val district: String?,
    val price: String?,
    val description: String?,
    val surface: String?,
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

//Ajouter ce champs dans le cas de l'utilisation de foreignKeys (plus haut)
//    val agentId: String
)
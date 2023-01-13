package com.openclassrooms.realestatemanager.data.dao

import androidx.room.*
import com.openclassrooms.realestatemanager.data.models.entities.PropertyPictureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureDao {

    @Query("SELECT * FROM PropertyPictureEntity WHERE propertyId = :propertyId")
    fun getAllPropertyPictures(propertyId: Long): Flow<List<PropertyPictureEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPropertyPicture(propertyPicture: PropertyPictureEntity)

    @Update
    suspend fun updatePropertyPicture(propertyPicture: PropertyPictureEntity)
}
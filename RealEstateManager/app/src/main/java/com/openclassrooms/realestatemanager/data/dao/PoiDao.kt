package com.openclassrooms.realestatemanager.data.dao

import androidx.room.*
import com.openclassrooms.realestatemanager.data.models.entities.PropertyPictureEntity
import kotlinx.coroutines.flow.Flow

//@Dao
//interface PoiDao {
//
//    @Query("SELECT * FROM PoiEntity WHERE propertyId = :propertyId")
//    fun getPoi(propertyId: Long): Flow<PoiEntity>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertPoi(poiEntity: PoiEntity)
//
//    @Update
//    suspend fun updatePoi(poiEntity: PoiEntity)
//
//    @Delete
//    suspend fun deletePoi(poi: PoiEntity)
//}
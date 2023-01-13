package com.openclassrooms.realestatemanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {
    @Query("SELECT * FROM PropertyEntity ORDER BY id ASC")
    fun getAllProperty(): Flow<List<PropertyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: PropertyEntity): Long

    @Update
    suspend fun updateProperty(property: PropertyEntity)

    @RawQuery(observedEntities = [PropertyEntity::class])
    fun getAllPropertyFilter(query: SupportSQLiteQuery): Flow<List<PropertyEntity>>

    @Query("SELECT id FROM PropertyEntity")
    fun getListId(): LiveData<List<Int>>

    @Query("SELECT MIN(price) as minPrice, MAX(price) as maxPrice, MIN(surface) as minSurface, MAX(surface) as maxSurface FROM PropertyEntity WHERE id IN (:listId)")
    fun getMinMaxPriceAndSurface(listId: List<Int>): Flow<PropertyPriceSurface>

    @Query("SELECT DISTINCT type FROM PropertyEntity")
    fun getListType(): Flow<List<String>>

    @Query("SELECT DISTINCT type FROM PropertyEntity ")
    fun getListTown(): Flow<List<String>>

}

data class PropertyPriceSurface(
    val minPrice: Int,
    val maxPrice: Int,
    val minSurface: Int,
    val maxSurface: Int,
)





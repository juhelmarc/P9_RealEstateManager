package com.openclassrooms.realestatemanager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyPicturesEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface RoomDao {

    @Query("SELECT * FROM PropertyEntity ORDER BY id ASC")
    fun getAllProperty(): Flow<List<PropertyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: PropertyEntity)

    @Update
    suspend fun updateProperty(property: PropertyEntity)

    @Query("SELECT * FROM PropertyPicturesEntity WHERE propertyId = :propertyId" )
    fun getAllPropertyPictures(propertyId: Long): Flow<List<PropertyPicturesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPropertyPicture(propertyPicture: PropertyPicturesEntity)

    @Update
    suspend fun updatePropertyPicture(property: PropertyPicturesEntity)

    @Query("SELECT * FROM AgentEntity WHERE agentId = :agentId")
    fun getAgentById(agentId: Long): Flow<AgentEntity>

    @Query("SELECT * FROM AgentEntity ORDER BY agentId ASC")
    fun getAllAgent(): Flow<List<AgentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgent(agent: AgentEntity)
}
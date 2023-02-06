package com.openclassrooms.realestatemanager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AgentDao {

    @Query("SELECT * FROM AgentEntity WHERE agentId = :agentId")
    fun getAgentById(agentId: Long): Flow<AgentEntity>

    @Query("SELECT * FROM AgentEntity ORDER BY agentId ASC")
    fun getAllAgent(): Flow<List<AgentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgent(agent: AgentEntity)
}
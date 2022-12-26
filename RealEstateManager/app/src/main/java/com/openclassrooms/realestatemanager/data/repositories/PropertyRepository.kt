package com.openclassrooms.realestatemanager.data.repositories

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.data.dao.RoomDao
import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyPicturesEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


class PropertyRepository @Inject constructor(
    private val roomDao: RoomDao
    ) {

    val getAllProperty: Flow<List<PropertyEntity>> = roomDao.getAllProperty()

    @WorkerThread
    suspend fun insertProperty(property: PropertyEntity) {
        roomDao.insertProperty(property)
    }

//    @WorkerThread
//    suspend fun updateProperty(property: PropertyEntity) {
//        roomDao.updateProperty(property)
//    }


    fun getPropertyByIdFlow(id: Long): Flow<PropertyEntity> = getAllProperty.map { propertiesList ->
        propertiesList.first { it.id == id }
    }

    fun getAllPicturesOfThisProperty(propertyId: Long): Flow<List<PropertyPicturesEntity>> {
       return roomDao.getAllPropertyPictures(propertyId)
    }

    fun getAgentById(agentId: Long): Flow<AgentEntity> {
        return roomDao.getAgentById(agentId)
    }

    fun getAllAgent(): Flow<List<AgentEntity>> {
        return roomDao.getAllAgent()
    }

    private val formPropertyIdMutableStateFlow = MutableStateFlow(0L)

    val formPropertyIdFlow: Long = formPropertyIdMutableStateFlow.value

    @MainThread
    fun setCurrentId(currentId: Long) {
        formPropertyIdMutableStateFlow.value = currentId
    }

}
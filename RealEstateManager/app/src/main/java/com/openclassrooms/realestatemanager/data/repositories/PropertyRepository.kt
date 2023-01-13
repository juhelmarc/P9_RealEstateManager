package com.openclassrooms.realestatemanager.data.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.data.dao.*
import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyPictureEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PropertyRepository @Inject constructor(
    private val agentDao: AgentDao,
    private val pictureDao: PictureDao,
    private val propertyDao: PropertyDao,
//    private val poiDao: PoiDao
    ) {

    val getAllProperty: Flow<List<PropertyEntity>> = propertyDao.getAllProperty()

    fun getAllPropertyFilter(query: String): Flow<List<PropertyEntity>> = propertyDao.getAllPropertyFilter(SimpleSQLiteQuery(query))

    @WorkerThread
    suspend fun insertProperty(property: PropertyEntity): Long = propertyDao.insertProperty(property)


    fun getPropertyByIdFlow(id: Long): Flow<PropertyEntity> = getAllProperty.map { propertiesList ->
        propertiesList.first { it.id == id }
    }

    @WorkerThread
    suspend fun insertPicture(picture: PropertyPictureEntity) {
        pictureDao.insertPropertyPicture(picture)
    }

    fun getAllPicturesOfThisProperty(propertyId: Long): Flow<List<PropertyPictureEntity>> {
       return pictureDao.getAllPropertyPictures(propertyId)
    }


    fun getAllAgent(): Flow<List<AgentEntity>> {
        return agentDao.getAllAgent()
    }

//    @WorkerThread
//    suspend fun insertPoi(poi: PoiEntity) {
//        poiDao.insertPoi(poi)
//    }

//    fun getPoiOfThisProperty(propertyId: Long): Flow<PoiEntity> {
//       return poiDao.getPoi(propertyId)
//    }
//    suspend fun deletePoi(poi: PoiEntity) {
//        poiDao.deletePoi(poi)
//    }

    private val formPropertyIdMutableLiveData = MutableLiveData<Long>()

    val currentIdLiveData: LiveData<Long> = formPropertyIdMutableLiveData

    fun setCurrentPropertyId(id: Long) {
        formPropertyIdMutableLiveData.value = id
    }

    private val queryMutableLiveData = MutableLiveData<String>()

    val queryFilterLiveData: LiveData<String> = queryMutableLiveData
    //Rassemblé le set et l'écoute de la requête
    fun setQueryFilterLiveData(query: String, isFiltered: Boolean): Flow<List<PropertyEntity>> {
        if(isFiltered) {
           return getAllPropertyFilter(query)
//            queryMutableLiveData.value = query
        } else {
          return  getAllProperty
//            queryMutableLiveData.value = "SELECT * FROM PropertyEntity ORDER BY id ASC"
        }

    }

    fun getListIdProperty() : LiveData<List<Int>> {
        return propertyDao.getListId()
    }

    fun getMinMaxPriceAndSurface(listId: List<Int>): Flow<PropertyPriceSurface> {
        return propertyDao.getMinMaxPriceAndSurface(listId)
    }

    fun getListType(): Flow<List<String>> {
        return propertyDao.getListType()
    }
    fun getListTown(): Flow<List<String>> {
        return propertyDao.getListTown()
    }

}
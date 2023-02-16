package com.openclassrooms.realestatemanager.data.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.data.dao.AgentDao
import com.openclassrooms.realestatemanager.data.dao.PictureDao
import com.openclassrooms.realestatemanager.data.dao.PropertyDao
import com.openclassrooms.realestatemanager.data.dao.PropertyPriceSurface
import com.openclassrooms.realestatemanager.data.models.CurrentFilterValue
import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyPictureEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PropertyRepository @Inject constructor(
    private val agentDao: AgentDao,
    private val pictureDao: PictureDao,
    private val propertyDao: PropertyDao,
) {

    fun getAllProperty(): Flow<List<PropertyEntity>> {
        return propertyDao.getAllProperty()
    }

    fun getAllPropertyFilter(query: String): Flow<List<PropertyEntity>> {
        return propertyDao.getAllPropertyFilter(SimpleSQLiteQuery(query))
    }

    @WorkerThread
    suspend fun insertProperty(property: PropertyEntity): Long =
        propertyDao.insertProperty(property)

    fun getPropertyByIdFlow(id: Long): Flow<PropertyEntity> {
        return getAllProperty().map { propertiesList ->
            propertiesList.first { it.id == id }
        }
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

    private val formPropertyIdMutableLiveData = MutableLiveData<Long>()

    fun getCurrentIdLiveData(): LiveData<Long> {
        return formPropertyIdMutableLiveData
    }

    fun setCurrentPropertyId(id: Long) {
        formPropertyIdMutableLiveData.value = id
    }

    private val isAnUpdatePropertyMutableStateLiveData = MutableLiveData<Boolean>()

    fun getIsAnUpdatePropertyLiveData(): LiveData<Boolean> {
        return isAnUpdatePropertyMutableStateLiveData
    }

    fun setIsAnUpdateProperty(isAnUpdate: Boolean) {
        isAnUpdatePropertyMutableStateLiveData.value = isAnUpdate
    }

    private val initialQuery = "SELECT * FROM PropertyEntity ORDER BY id ASC"

    private val queryFilterMutableLiveData = MutableLiveData(initialQuery)

    fun getQueryFilter(): LiveData<String> {
        return queryFilterMutableLiveData
    }

    fun registerFilterQueryWhenSubmitButtonClicked(query: String) {
        queryFilterMutableLiveData.value = query
    }

    private val initialFilter = CurrentFilterValue(
        minPriceSelected = null,
        maxPriceSelected = null,
        minSurfaceSelected = null,
        maxSurfaceSelected = null,
        agentNameSelected = "",
        listOfTypeSelected = listOf(),
        listOfTownSelected = listOf()
    )

    private val currentFilterValueMutableStateFlow =
        MutableStateFlow(initialFilter)

    fun getCurrentFilterValue(): Flow<CurrentFilterValue> {
        return currentFilterValueMutableStateFlow
    }

    fun deleteCurrentFilter() {
        currentFilterValueMutableStateFlow.value = initialFilter
        queryFilterMutableLiveData.value = initialQuery
    }

    fun registerCurrentFilterValue(value: CurrentFilterValue) {
        currentFilterValueMutableStateFlow.value = value
    }

    fun getMinMaxPriceAndSurface(): Flow<PropertyPriceSurface> {
        return propertyDao.getMinMaxPriceAndSurface()
    }

    fun getListType(): Flow<List<String>> {
        return propertyDao.getListType()
    }

    fun getListTown(): Flow<List<String>> {
        return propertyDao.getListTown()
    }
}
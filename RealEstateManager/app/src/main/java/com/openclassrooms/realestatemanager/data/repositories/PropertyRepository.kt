package com.openclassrooms.realestatemanager.data.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.data.dao.*
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

    private val formPropertyIdMutableLiveData = MutableLiveData<Long>()

    val currentIdLiveData: LiveData<Long> = formPropertyIdMutableLiveData

    fun setCurrentPropertyId(id: Long) {
        formPropertyIdMutableLiveData.value = id
    }

    private val isAnUpdatePropertyMutableStateLiveData = MutableLiveData<Boolean>()

    val isAnUpdatePropertyLiveData: LiveData<Boolean> = isAnUpdatePropertyMutableStateLiveData

    fun setIsAnUpdateProperty(isAnUpdate: Boolean) {
        isAnUpdatePropertyMutableStateLiveData.value = isAnUpdate
    }

    private val initialQuery = "SELECT * FROM PropertyEntity ORDER BY id ASC"

    private val queryFilterMutableLiveData = MutableLiveData<String>(initialQuery)

    val queryFilterLiveData: LiveData<String> = queryFilterMutableLiveData

    private val queryFilterListFragmentMutableLiveData = MutableLiveData<String>(initialQuery)

    val queryFilterListFragmentLiveData: LiveData<String> = queryFilterListFragmentMutableLiveData

    fun registerFilterQueryWhenSubmitButtonClicked(query: String) {
        queryFilterListFragmentMutableLiveData.value = query
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

    private val currentFilterValueMutableLiveData = MutableStateFlow<CurrentFilterValue>(initialFilter)

    val currentFilterValue: Flow<CurrentFilterValue> = currentFilterValueMutableLiveData

    fun deleteCurrentFilter() {
        currentFilterValueMutableLiveData.value = initialFilter
        queryFilterMutableLiveData.value = initialQuery
        queryFilterListFragmentMutableLiveData.value = initialQuery
    }
    fun registerCurrentFilterValueAndQuery(query: String, value: CurrentFilterValue) {
        queryFilterMutableLiveData.value = query
        currentFilterValueMutableLiveData.value = value
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
package com.openclassrooms.realestatemanager.ui.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import com.openclassrooms.realestatemanager.AgentFixtures
import com.openclassrooms.realestatemanager.FilterFixtures
import com.openclassrooms.realestatemanager.PropertyFixtures
import com.openclassrooms.realestatemanager.PropertyPictureFixtures
import com.openclassrooms.realestatemanager.data.dao.AgentDao
import com.openclassrooms.realestatemanager.data.dao.PictureDao
import com.openclassrooms.realestatemanager.data.dao.PropertyDao
import com.openclassrooms.realestatemanager.data.dao.PropertyPriceSurface
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class PropertyRepositoryTests {

    private val agentDao: AgentDao = mock()
    private val pictureDao: PictureDao = mock()
    private val propertyDao: PropertyDao = mock()

    private val propertyRepository = PropertyRepository(agentDao, pictureDao, propertyDao)

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(agentDao, pictureDao, propertyDao)
    }

    @Test
    fun getAllPropertySuccess() = runTest {
        val expectedPropertyList = listOf(PropertyFixtures.PropertyEntityUtils.create())
        whenever(propertyDao.getAllProperty()).thenReturn(flowOf(expectedPropertyList))

        val propertyList = propertyRepository.getAllProperty().toList()[0]

        verify(propertyDao).getAllProperty()
        Assert.assertSame(expectedPropertyList, propertyList)
        assertEquals(expectedPropertyList, propertyList)
    }

    @Test
    fun getAllPropertyFilterSuccess() = runTest {
        val property = PropertyFixtures.PropertyEntityUtils.create()
        val expectedPropertyList = listOf(property)
        whenever(propertyDao.getAllPropertyFilter(any())).thenReturn(flowOf(expectedPropertyList))

        val propertyList : List<PropertyEntity> = propertyRepository.getAllPropertyFilter("").toList()[0]

        verify(propertyDao).getAllPropertyFilter(any())
        assertEquals(expectedPropertyList, propertyList)
    }

    @Test
    fun insertPropertySuccess() = runTest {
        val expectedProperty: PropertyEntity = PropertyFixtures.PropertyEntityUtils.create()

        propertyRepository.insertProperty(expectedProperty)

        Mockito.verify(propertyDao).insertProperty(expectedProperty)
    }

    @Test
    fun getPropertyByIdFlowSuccess() = runTest {
        val expectedProperty = PropertyFixtures.PropertyEntityUtils.create().copy(id = 1L)
        val propertyList = listOf(expectedProperty)
        whenever(propertyDao.getAllProperty()).thenReturn(flowOf(propertyList))

        val property = propertyRepository.getPropertyByIdFlow(1L).first()

        verify(propertyDao).getAllProperty()
        assertEquals(expectedProperty, property)
    }

    @Test
    fun insertPictureWithSuccess() = runTest {
        val expectedPicture = PropertyPictureFixtures.PictureUtils1.create()

        propertyRepository.insertPicture(expectedPicture)

        verify(pictureDao).insertPropertyPicture(expectedPicture)
    }

    @Test
    fun getAllPicturesOfThisPropertySuccess() = runTest {
        val expectedListPicture = PropertyPictureFixtures.ListPictureUtils.create()
        val propertyId = PropertyPictureFixtures.PictureForeignKeyUtils.create()
        whenever(pictureDao.getAllPropertyPictures(propertyId)).thenReturn(flowOf(expectedListPicture))

        val listPicture = propertyRepository.getAllPicturesOfThisProperty(propertyId).toList()[0]

        verify(pictureDao).getAllPropertyPictures(propertyId)
        assertEquals(expectedListPicture, listPicture)
    }

    @Test
    fun getAllAgentSuccess() = runTest {
        val expectedAgentList = AgentFixtures.AgentListUtils.create()
        whenever(agentDao.getAllAgent()).thenReturn(flowOf(expectedAgentList))


        val agentList = propertyRepository.getAllAgent().toList()[0]

        verify(agentDao).getAllAgent()
        assertEquals(expectedAgentList, agentList)
    }

    @Test
    fun setAndGetCurrentIdSuccess() {
        val expectedId = PropertyFixtures.PropertyEntityUtils.create().id

        propertyRepository.setCurrentPropertyId(expectedId)
        val id = propertyRepository.getCurrentIdLiveData()

        assertEquals(expectedId, id.value)
    }

    @Test
    fun setAndGetIsAnUpdatePropertySuccess() {
        val expectedIsAnUpdate = true

        propertyRepository.setIsAnUpdateProperty(expectedIsAnUpdate)
        val isAnUpdate = propertyRepository.getIsAnUpdatePropertyLiveData()

        assertEquals(expectedIsAnUpdate, isAnUpdate.value)
    }

    @Test
    fun setAndGetQueryFilterSuccess() {
        val expectedQuery = ""

        propertyRepository.registerFilterQueryWhenSubmitButtonClicked(expectedQuery)
        val query = propertyRepository.getQueryFilter()

        assertEquals(expectedQuery, query.value)
    }

    @Test
    fun setAndGetCurrentFilterValueSuccess() = runTest {
        val expectedFilterValue = FilterFixtures.CurrentFilterValueUtils.create()

        propertyRepository.registerCurrentFilterValue( expectedFilterValue)
        val filterValue  = propertyRepository.getCurrentFilterValue().first()

        assertEquals(expectedFilterValue, filterValue)
    }

    @Test
    fun deleteCurrentQueryAndValueSuccess() = runTest {
        val expectedFilterValueAfterDelete = FilterFixtures.DefaultFilterValueUtils.create()
        val expectedFilterQueryAfterDelete = FilterFixtures.DefaultFilterQueryUtils.create()
        val currentFilterValueInserted = FilterFixtures.CurrentFilterValueUtils.create()
        val currentFilterQueryInserted = FilterFixtures.CurrentFilterQueryUtils.create()

        propertyRepository.registerCurrentFilterValue(currentFilterValueInserted)
        propertyRepository.registerFilterQueryWhenSubmitButtonClicked(currentFilterQueryInserted)
        propertyRepository.deleteCurrentFilter()
        val filterValueAfterDelete = propertyRepository.getCurrentFilterValue().first()
        val filterQueryAfterDelete = propertyRepository.getQueryFilter()

        assertEquals(expectedFilterValueAfterDelete, filterValueAfterDelete)
        assertEquals(expectedFilterQueryAfterDelete, filterQueryAfterDelete.value)

        assertNotSame(expectedFilterQueryAfterDelete, currentFilterQueryInserted)
        assertNotSame(filterQueryAfterDelete.value, currentFilterQueryInserted)
        assertNotSame(expectedFilterValueAfterDelete, currentFilterValueInserted)
        assertNotSame(filterValueAfterDelete, currentFilterValueInserted)
    }

    @Test
    fun getMinMaxPriceAndSurfaceSuccess() = runTest {
        val expectedMinMaxPriceSurface = PropertyPriceSurface(1, 1, 1, 1)
        whenever(propertyDao.getMinMaxPriceAndSurface()).thenReturn(flowOf(expectedMinMaxPriceSurface))

        val minMaxPriceSurface = propertyRepository.getMinMaxPriceAndSurface().first()

        verify(propertyDao).getMinMaxPriceAndSurface()
        assertEquals(expectedMinMaxPriceSurface, minMaxPriceSurface)
    }

    @Test
    fun getListTypeSuccess() = runTest {
        val expectedListType = listOf<String>()
        whenever(propertyDao.getListType()).thenReturn(flowOf(expectedListType))

        val listType = propertyRepository.getListType().toList()[0]

        verify(propertyDao).getListType()
        assertEquals(expectedListType, listType)
    }

    @Test
    fun getListTownSuccess() = runTest {
        val expectedListTown = listOf<String>()
        whenever(propertyDao.getListTown()).thenReturn(flowOf(expectedListTown))

        val listTown = propertyRepository.getListTown().toList()[0]

        verify(propertyDao).getListTown()
        assertEquals(expectedListTown, listTown)
    }
}
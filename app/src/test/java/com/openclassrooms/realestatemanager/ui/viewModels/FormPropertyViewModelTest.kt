package com.openclassrooms.realestatemanager.ui.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.*
import com.openclassrooms.realestatemanager.AgentFixtures
import com.openclassrooms.realestatemanager.PropertyFixtures
import com.openclassrooms.realestatemanager.PropertyPictureFixtures
import com.openclassrooms.realestatemanager.data.PoiList
import com.openclassrooms.realestatemanager.data.models.entities.PropertyPictureEntity
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import com.openclassrooms.realestatemanager.ui.formProperty.FormPropertyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class FormPropertyViewModelTest {

    private val propertyRepository : PropertyRepository = mock()

    private lateinit var viewModel: FormPropertyViewModel

    private val dispatcher : TestDispatcher = UnconfinedTestDispatcher()

    companion object {
        val formViewStateUpdate = PropertyFixtures.FormPropertyViewStateUpdateUtils.create()
        val formViewStateEdit = PropertyFixtures.FormPropertyViewStateEditUtils.create()
        val property = PropertyFixtures.PropertyEntityUtils.create()
        val pictureList = PropertyPictureFixtures.ListPictureUtils.create()

    }

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = FormPropertyViewModel(propertyRepository)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(propertyRepository)
        Dispatchers.resetMain()
    }

    @Test
    fun getInitialViewStateUpdateSuccess() = runTest {
        val expectedViewState = formViewStateUpdate
        whenever(propertyRepository.getCurrentIdLiveData()).thenReturn(MutableLiveData(formViewStateUpdate.id))
        whenever(propertyRepository.getIsAnUpdatePropertyLiveData()).thenReturn(MutableLiveData(true))
        whenever(propertyRepository.getPropertyByIdFlow(formViewStateUpdate.id)).thenReturn(flowOf(property))
        whenever(propertyRepository.getAllPicturesOfThisProperty(formViewStateUpdate.id)).thenReturn(flowOf(pictureList))

        val liveData = viewModel.getInitialViewStateLiveData()
        liveData.observeForever {  }
        val viewState = liveData.value

        verify(propertyRepository).getCurrentIdLiveData()
        verify(propertyRepository).getIsAnUpdatePropertyLiveData()
        verify(propertyRepository).getPropertyByIdFlow(formViewStateUpdate.id)
        verify(propertyRepository).getAllPicturesOfThisProperty(formViewStateUpdate.id)
        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun getInitialViewStateEditSuccess() = runTest {
        val expectedViewState = formViewStateEdit
        whenever(propertyRepository.getCurrentIdLiveData()).thenReturn(MutableLiveData(formViewStateUpdate.id))
        whenever(propertyRepository.getIsAnUpdatePropertyLiveData()).thenReturn(MutableLiveData(false))

        val liveData = viewModel.getInitialViewStateLiveData()
        liveData.observeForever {  }
        val viewState = liveData.value

        verify(propertyRepository).getCurrentIdLiveData()
        verify(propertyRepository).getIsAnUpdatePropertyLiveData()
        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun getAgentListSuccess() = runTest {
        val expectedAgentList = AgentFixtures.AgentListUtils.create()
        whenever(propertyRepository.getAllAgent()).thenReturn(flowOf(expectedAgentList))

        val liveData = viewModel.getAgentListLiveData()
        liveData.observeForever {  }
        val agentList = liveData.value


        verify(propertyRepository).getAllAgent()
        assertEquals(expectedAgentList, agentList)
    }

    @Test
    fun getIsAnUpdateSuccess() {
        val expectedIsAnUpdate = true
        whenever(propertyRepository.getIsAnUpdatePropertyLiveData()).thenReturn(MutableLiveData(expectedIsAnUpdate))

        val isAnUpdate = viewModel.getIsAnUpdate()

        verify(propertyRepository).getIsAnUpdatePropertyLiveData()
        assertEquals(expectedIsAnUpdate, isAnUpdate)
    }

    @Test
    fun setAndGetInitialViewStateSuccess() = runTest {
        val expectedViewState = formViewStateUpdate
        val expectedEmptyViewState = formViewStateEdit

        viewModel.setInitialViewState(expectedEmptyViewState)
        val viewStateEmpty = viewModel.getPropertyViewState()
        viewModel.setInitialViewState(expectedViewState)
        val viewState = viewModel.getPropertyViewState()

        assertEquals(expectedEmptyViewState, viewStateEmpty)
        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun getViewStateLiveDataSuccess() = runTest {
        val expectedViewState = formViewStateUpdate

        viewModel.setInitialViewState(expectedViewState)
        val liveData = viewModel.getViewStateLiveData()
        liveData.observeForever {  }
        val viewState = liveData.value

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun updateListPictureSuccess() {
        val picture = PropertyPictureFixtures.PictureUtils1.create().url
        val expectedListPicture = mutableListOf<PropertyPictureEntity>()
        expectedListPicture.addAll(formViewStateUpdate.listPicture)
        expectedListPicture.add(PropertyPictureEntity(0L, 0L, picture, ""))
        viewModel.setInitialViewState(formViewStateUpdate)

        viewModel.updateListPicture(picture)
        val listPicture = viewModel.getPropertyViewState().listPicture

        assertEquals(expectedListPicture, listPicture)
    }

//    @Test
//    fun updatePropertyEntitySuccess() = runTest {
//        val expectedPropertyEntity = PropertyFixtures.PropertyEntityUtils.create()
//        val expectedViewState = formViewStateUpdate
//        whenever(propertyRepository.insertProperty(expectedPropertyEntity))
//        expectedViewState.listPicture.forEach { picture ->
//            val pictureToInsert = picture.copy(propertyId = expectedPropertyEntity.id)
//            whenever(propertyRepository.insertPicture(pictureToInsert))
//        }
//
//        viewModel.updatePropertyEntity(expectedViewState)
//
//        verify(propertyRepository).insertProperty(expectedPropertyEntity)
//        verify(propertyRepository, atLeast(expectedViewState.listPicture.size)).insertPicture(any())
//    }


}
package com.openclassrooms.realestatemanager.ui.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.*
import com.openclassrooms.realestatemanager.PropertyPictureFixtures
import com.openclassrooms.realestatemanager.data.repositories.CurrentPropertyRepository
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import com.openclassrooms.realestatemanager.ui.detailslider.DetailSliderViewModel
import com.openclassrooms.realestatemanager.ui.detailslider.DetailSliderViewState
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
class DetailSliderViewModelTest {


    private val propertyRepository : PropertyRepository = mock()

    private val currentPropertyRepository : CurrentPropertyRepository = mock()

    private lateinit var viewModel : DetailSliderViewModel

    private val dispatcher : TestDispatcher = UnconfinedTestDispatcher()

    companion object {

        private val listPictureEntity = PropertyPictureFixtures.ListPictureUtils.create()
        private val propertyId = listPictureEntity[0].propertyId
        private val nullId = null

    }

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = DetailSliderViewModel(propertyRepository, currentPropertyRepository)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(propertyRepository, currentPropertyRepository)
        Dispatchers.resetMain()
    }

    @Test
    fun getDetailSliderSuccess() = runTest {
        val expectedViewState = DetailSliderViewState(listPictureEntity)
        whenever(currentPropertyRepository.currentIdLiveData).thenReturn(MutableLiveData(propertyId))
        whenever(propertyRepository.getAllPicturesOfThisProperty(propertyId)).thenReturn(flowOf(listPictureEntity))

        val detailSliderLiveData = viewModel.getDetailSlider()
        detailSliderLiveData.observeForever {  }
        val viewState = detailSliderLiveData.value

        verify(currentPropertyRepository, atLeast(2)).currentIdLiveData
        verify(propertyRepository).getAllPicturesOfThisProperty(propertyId)
        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun getDefaultSliderSuccess() = runTest {
        val expectedViewState = DetailSliderViewState(listPictureEntity)
        whenever(currentPropertyRepository.currentIdLiveData).thenReturn(MutableLiveData(nullId))
        whenever(propertyRepository.getAllPicturesOfThisProperty(propertyId)).thenReturn(flowOf(listPictureEntity))

        val detailSliderLiveData = viewModel.getDetailSlider()
        detailSliderLiveData.observeForever {  }
        val viewState = detailSliderLiveData.value

        verify(currentPropertyRepository).currentIdLiveData
        verify(propertyRepository).getAllPicturesOfThisProperty(propertyId)
        assertEquals(expectedViewState, viewState)
    }
}
package com.openclassrooms.realestatemanager.ui.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.*
import com.openclassrooms.realestatemanager.PropertyFixtures
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import com.openclassrooms.realestatemanager.ui.detail.DetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    private val propertyRepository : PropertyRepository = mock()

    private lateinit var viewModel: DetailViewModel

    private val dispatcher : TestDispatcher = UnconfinedTestDispatcher()

    companion object {

        val detailViewStateSelected = PropertyFixtures.DetailViewStateSelectedUtils.create()
        val detailViewStateEmpty = PropertyFixtures.DetailViewStateEmptyUtils.create()
    }

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = DetailViewModel(propertyRepository)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(propertyRepository)
        Dispatchers.resetMain()
    }

    @Test
    fun getDetailLiveDataSelectedSuccess() = runTest {
        val expectedViewState = detailViewStateSelected
        whenever(propertyRepository.getCurrentIdLiveData()).thenReturn(MutableLiveData(detailViewStateSelected.id))
        whenever(propertyRepository.getPropertyByIdFlow(any())).thenReturn(flowOf(PropertyFixtures.PropertyEntityUtils.create()))
        whenever(propertyRepository.getAllPicturesOfThisProperty(any())).thenReturn(flowOf(detailViewStateSelected.listPicture))

        val detailLiveData = viewModel.getDetailLiveData()
        detailLiveData.observeForever {  }
        val viewState = detailLiveData.value

        verify(propertyRepository).getCurrentIdLiveData()
        verify(propertyRepository).getPropertyByIdFlow(any())
        verify(propertyRepository).getAllPicturesOfThisProperty(any())
        Assert.assertEquals(expectedViewState, viewState)
    }

    @Test
    fun getDetailLiveDataEmptySuccess() = runTest {
        val expectedViewState =  detailViewStateEmpty
        whenever(propertyRepository.getCurrentIdLiveData()).thenReturn(MutableLiveData(0L))
        whenever(propertyRepository.getAllProperty()).thenReturn(flowOf(emptyList()))

        val detailLiveData = viewModel.getDetailLiveData()
        detailLiveData.observeForever {  }
        val viewState = detailLiveData.value

        verify(propertyRepository).getCurrentIdLiveData()
        verify(propertyRepository).getAllProperty()
        Assert.assertEquals(expectedViewState, viewState)
    }

    @Test
    fun getDetailLiveDataWhenIdZeroAndPropertyListNotEmpty() = runTest {
        val expectedViewState = detailViewStateEmpty
        whenever(propertyRepository.getCurrentIdLiveData()).thenReturn(MutableLiveData(0L))
        whenever(propertyRepository.getAllProperty()).thenReturn(flowOf(listOf(PropertyFixtures.PropertyEntityUtils.create())))

        val detailLiveData = viewModel.getDetailLiveData()
        detailLiveData.observeForever {  }
        val viewState = detailLiveData.value

        verify(propertyRepository).getCurrentIdLiveData()
        verify(propertyRepository).getAllProperty()
        verify(propertyRepository).setCurrentPropertyId(1L)
        Assert.assertEquals(expectedViewState, viewState)
    }

    @Test
    fun setIsAnUpdateSuccess() = runTest {
        val expectedIsAnUpdate = true

        viewModel.setIsAnUpdate(expectedIsAnUpdate)

        verify(propertyRepository).setIsAnUpdateProperty(expectedIsAnUpdate)
    }
}
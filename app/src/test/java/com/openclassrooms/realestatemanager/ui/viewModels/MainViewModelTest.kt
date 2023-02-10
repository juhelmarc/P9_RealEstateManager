package com.openclassrooms.realestatemanager.ui.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.openclassrooms.realestatemanager.PropertyFixtures
import com.openclassrooms.realestatemanager.data.repositories.CurrentPropertyRepository
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import com.openclassrooms.realestatemanager.ui.main.MainViewModel
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
class MainViewModelTest {

    private val propertyRepository : PropertyRepository = mock()

    private val currentPropertyRepository : CurrentPropertyRepository = mock()

    private lateinit var viewModel : MainViewModel

    private val dispatcher : TestDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        whenever(currentPropertyRepository.currentIdLiveData).thenReturn(MutableLiveData(1L))
        viewModel = MainViewModel(currentPropertyRepository, propertyRepository)
    }

    @After
    fun tearDown() {
        verify(currentPropertyRepository).currentIdLiveData
        verifyNoMoreInteractions(propertyRepository, currentPropertyRepository)
        Dispatchers.resetMain()
    }

    @Test
    fun getListPropertyLiveDataSuccess() = runTest {
        val expectedListProperty = listOf(PropertyFixtures.PropertyEntityUtils.create())
        whenever(propertyRepository.getAllProperty()).thenReturn(flowOf(expectedListProperty))

        val liveData = viewModel.getListPropertyLiveData()
        liveData.observeForever {  }
        val listProperty = liveData.value

        verify(propertyRepository).getAllProperty()
        assertEquals(expectedListProperty, listProperty)
    }

    @Test
    fun setIsAnUpdatePropertySuccess() {
        viewModel.setIsAnUpdateProperty(true)

        verify(propertyRepository).setIsAnUpdateProperty(true)
    }

    @Test
    fun deleteCurrentFilterSuccess() {
        viewModel.deleteCurrentFilter()

        verify(propertyRepository).deleteCurrentFilter()
    }

    @Test
    fun setCurrentPropertyIdSuccess() {
        viewModel.setCurrentPropertyId(1L)

        verify(propertyRepository).setCurrentPropertyId(1L)
    }

}
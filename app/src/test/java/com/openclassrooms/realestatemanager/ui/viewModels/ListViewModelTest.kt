package com.openclassrooms.realestatemanager.ui.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.*
import com.openclassrooms.realestatemanager.PropertyFixtures
import com.openclassrooms.realestatemanager.data.repositories.CurrentPropertyRepository
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import com.openclassrooms.realestatemanager.ui.list.ListViewModel
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
class ListViewModelTest {

    private val propertyRepository : PropertyRepository = mock()

    private val currentPropertyRepository : CurrentPropertyRepository = mock()

    private lateinit var viewModel : ListViewModel

    private val dispatcher : TestDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = ListViewModel(propertyRepository, currentPropertyRepository)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(propertyRepository, currentPropertyRepository)
        Dispatchers.resetMain()
    }

    @Test
    fun getPropertyListSuccess() = runTest {
        val expectedListViewState = listOf(PropertyFixtures.ListViewStateUtils.create())
        val listProperty = listOf(PropertyFixtures.PropertyEntityUtils.create())
        whenever(propertyRepository.getQueryFilter()).thenReturn(MutableLiveData(""))
        whenever(propertyRepository.getAllPropertyFilter("")).thenReturn(flowOf(listProperty))

        val liveData = viewModel.getPropertyListFilterLiveData()
        liveData.observeForever {  }
        val viewState = liveData.value

        verify(propertyRepository).getQueryFilter()
        verify(propertyRepository).getAllPropertyFilter("")
        assertEquals(expectedListViewState, viewState)
    }

    @Test
    fun onItemClickedSuccess() {
        viewModel.onItemClicked(1L)

        verify(currentPropertyRepository).setCurrentId(1L)
        verify(propertyRepository).setCurrentPropertyId(1L)
    }

}
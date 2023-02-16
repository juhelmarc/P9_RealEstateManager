package com.openclassrooms.realestatemanager.ui.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import com.openclassrooms.realestatemanager.AgentFixtures
import com.openclassrooms.realestatemanager.FilterFixtures
import com.openclassrooms.realestatemanager.PropertyFixtures
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import com.openclassrooms.realestatemanager.ui.filter.FilterViewModel
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
class FilterViewModelTest {

    private val propertyRepository : PropertyRepository = mock()

    private lateinit var viewModel: FilterViewModel

    private val dispatcher : TestDispatcher = UnconfinedTestDispatcher()

    companion object {

        val minMaxPriceSurface = FilterFixtures.MinMaxPriceSurfaceUtils.create()
        val agentList = AgentFixtures.AgentListUtils.create()
        val listType = FilterFixtures.ListTypeUtils.create()
        val listTown = FilterFixtures.ListTownUtils.create()
        val currentFilterValue = FilterFixtures.CurrentFilterValueUtils.create()

    }

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = FilterViewModel(propertyRepository)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(propertyRepository)
        Dispatchers.resetMain()
    }

    @Test
    fun getFilterFeatureViewStateSuccess() = runTest {
        val expectedFilterViewState = FilterFixtures.FilterFeatureUtils.create()
        whenever(propertyRepository.getMinMaxPriceAndSurface()).thenReturn(flowOf(minMaxPriceSurface))
        whenever(propertyRepository.getAllAgent()).thenReturn(flowOf(agentList))
        whenever(propertyRepository.getListType()).thenReturn(flowOf(listType))
        whenever(propertyRepository.getListTown()).thenReturn(flowOf(listTown))
        whenever(propertyRepository.getCurrentFilterValue()).thenReturn(flowOf(currentFilterValue))

        val filterFeatureLiveData = viewModel.getFilterFeatureViewStateLiveData()
        filterFeatureLiveData.observeForever {  }
        val viewState = filterFeatureLiveData.value

        verify(propertyRepository).getMinMaxPriceAndSurface()
        verify(propertyRepository).getAllAgent()
        verify(propertyRepository).getListType()
        verify(propertyRepository).getListTown()
        verify(propertyRepository).getCurrentFilterValue()
        assertEquals(expectedFilterViewState, viewState)
    }

    @Test
    fun registerCurrentFilterValueSuccess() = runTest {
        val expectedFilterViewState = FilterFixtures.FilterFeatureUtils.create()
        val expectedFilterValue = currentFilterValue

        viewModel.registerCurrentFilterValue(expectedFilterViewState)

        verify(propertyRepository).registerCurrentFilterValue(expectedFilterValue)
    }

    @Test
    fun toQuerySuccess() {
        val expectedQuery = "SELECT * FROM PropertyEntity WHERE (price BETWEEN 1 AND 1)"+
                " AND (surface BETWEEN 1 AND 1)" +
                " AND (agentName = 'Test')" +
                " And (type in ('test'))" +
                " And (town in ('test'))"
        val viewState = FilterFixtures.FilterFeatureUtils.create()

        val query = viewModel.toQuery(viewState)

        assertEquals(expectedQuery, query)
    }

    @Test
    fun registerFilterQuerySuccess() {
        val expectedQuery = ""

        viewModel.registerFilterQueryWhenSubmitButtonClicked(expectedQuery)

        verify(propertyRepository).registerFilterQueryWhenSubmitButtonClicked(expectedQuery)
    }

    @Test
    fun deleteCurrentFilterSuccess()  {
        viewModel.deleteCurrentFilter()

        verify(propertyRepository).deleteCurrentFilter()
    }

    @Test
    fun getNbrOfPropertyWithThisQuerySuccess() = runTest {
        val listProperty = listOf(PropertyFixtures.PropertyEntityUtils.create())
        val expectedSize = listOf(PropertyFixtures.PropertyEntityUtils.create()).size
        val query = ""
        whenever(propertyRepository.getAllPropertyFilter(query)).thenReturn(flowOf(listProperty))

        val liveData = viewModel.getNbrOfPropertyWithThisQuery(query)
        liveData.observeForever {  }
        val propertySize = liveData.value

        verify(propertyRepository).getAllPropertyFilter(query)
        assertEquals(expectedSize, propertySize)
    }

    @Test
    fun updateFilterPriceSuccess() {
        val minPrice = 0
        val maxPrice = 10
        val filterViewState = FilterFixtures.FilterFeatureUtils.create()
        val expectedFilterViewState = FilterFixtures.FilterFeatureUtils.create()
            .copy(
                minPriceSelected = minPrice,
                maxPriceSelected = maxPrice)
        viewModel.registerCurrentFilterValue(filterViewState)

        viewModel.updateFilterPrice(minPrice, maxPrice)
        val viewState = viewModel.getSavedCurrentFilter()

        verify(propertyRepository, atLeast(2) ).registerCurrentFilterValue(any())
        assertEquals(expectedFilterViewState, viewState)
    }

    @Test
    fun updateFilterSurfaceSuccess() {
        val minSurface = 0
        val maxSurface = 10
        val filterViewState = FilterFixtures.FilterFeatureUtils.create()
        val expectedFilterViewState = FilterFixtures.FilterFeatureUtils.create()
            .copy(
                minSurfaceSelected = minSurface,
                maxSurfaceSelected = maxSurface)
        viewModel.registerCurrentFilterValue(filterViewState)

        viewModel.updateFilterSurface(minSurface, maxSurface)
        val viewState = viewModel.getSavedCurrentFilter()

        verify(propertyRepository, atLeast(2) ).registerCurrentFilterValue(any())
        assertEquals(expectedFilterViewState, viewState)
    }

    @Test
    fun updateFilterAgentSuccess() {
        val agentName = "AgentName"
        val filterViewState = FilterFixtures.FilterFeatureUtils.create()
        val expectedFilterViewState = FilterFixtures.FilterFeatureUtils.create()
            .copy(agentNameSelected = agentName)
        viewModel.registerCurrentFilterValue(filterViewState)

        viewModel.updateFilterAgent(agentName)
        val viewState = viewModel.getSavedCurrentFilter()

        verify(propertyRepository, atLeast(2) ).registerCurrentFilterValue(any())
        assertEquals(expectedFilterViewState, viewState)
    }

    @Test
    fun updateFilterListTypeSuccess() {
        val listType = listOf("TypeTest", "TestType")
        val filterViewState = FilterFixtures.FilterFeatureUtils.create()
        val expectedFilterViewState = FilterFixtures.FilterFeatureUtils.create()
            .copy(listOfTypeSelected = listType)
        viewModel.registerCurrentFilterValue(filterViewState)

        viewModel.updateFilterListType(listType)
        val viewState = viewModel.getSavedCurrentFilter()

        verify(propertyRepository, atLeast(2) ).registerCurrentFilterValue(any())
        assertEquals(expectedFilterViewState, viewState)
    }

    @Test
    fun updateFilterListTownSuccess() {
        val listTown = listOf("TownTest", "TestTown")
        val filterViewState = FilterFixtures.FilterFeatureUtils.create()
        val expectedFilterViewState = FilterFixtures.FilterFeatureUtils.create()
            .copy(listOfTownSelected =  listTown)
        viewModel.registerCurrentFilterValue(filterViewState)

        viewModel.updateFilterListTown(listTown)
        val viewState = viewModel.getSavedCurrentFilter()

        verify(propertyRepository, atLeast(2) ).registerCurrentFilterValue(any())
        assertEquals(expectedFilterViewState, viewState)
    }

}
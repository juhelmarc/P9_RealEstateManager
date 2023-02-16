package com.openclassrooms.realestatemanager.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.data.models.CurrentFilterValue
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository
) : ViewModel() {

    fun getFilterFeatureViewStateLiveData() : LiveData<FilterFeatureViewState> {
       return combine(
            propertyRepository.getMinMaxPriceAndSurface(),
            propertyRepository.getAllAgent(),
            propertyRepository.getListType(),
            propertyRepository.getListTown(),
            propertyRepository.getCurrentFilterValue(),
        ) { minMaxPriceSurface, listAgent, listType, listTown, currentFilterValue ->
            FilterFeatureViewState(
                minPrice = minMaxPriceSurface.minPrice,
                maxPrice = minMaxPriceSurface.maxPrice,
                minSurface = minMaxPriceSurface.minSurface,
                maxSurface = minMaxPriceSurface.maxSurface,
                listAgent = listAgent,
                listOfType = listType,
                listOfTown = listTown,
                minPriceSelected = if (currentFilterValue.minPriceSelected != null && currentFilterValue.minPriceSelected >= minMaxPriceSurface.minPrice) currentFilterValue.minPriceSelected else minMaxPriceSurface.minPrice,
                maxPriceSelected = if (currentFilterValue.maxPriceSelected != null && currentFilterValue.maxPriceSelected <= minMaxPriceSurface.maxPrice) currentFilterValue.maxPriceSelected else minMaxPriceSurface.maxPrice,
                minSurfaceSelected = if (currentFilterValue.minSurfaceSelected != null && currentFilterValue.minSurfaceSelected >= minMaxPriceSurface.minSurface) currentFilterValue.minSurfaceSelected else minMaxPriceSurface.minSurface,
                maxSurfaceSelected = if (currentFilterValue.maxSurfaceSelected != null && currentFilterValue.maxSurfaceSelected <= minMaxPriceSurface.maxSurface) currentFilterValue.maxSurfaceSelected else minMaxPriceSurface.maxSurface,
                agentNameSelected = currentFilterValue.agentNameSelected,
                listOfTypeSelected = currentFilterValue.listOfTypeSelected,
                listOfTownSelected = currentFilterValue.listOfTownSelected,
            )
        }.asLiveData()
    }

    fun registerCurrentFilterValue(filterFeatureViewState: FilterFeatureViewState) {
        val currentFilterValue = CurrentFilterValue(
            minPriceSelected = filterFeatureViewState.minPriceSelected,
            maxPriceSelected = filterFeatureViewState.maxPriceSelected,
            minSurfaceSelected = filterFeatureViewState.minSurfaceSelected,
            maxSurfaceSelected = filterFeatureViewState.maxSurfaceSelected,
            agentNameSelected = filterFeatureViewState.agentNameSelected,
            listOfTypeSelected = filterFeatureViewState.listOfTypeSelected,
            listOfTownSelected = filterFeatureViewState.listOfTownSelected,
        )
        propertyRepository.registerCurrentFilterValue(currentFilterValue)
        savedCurrentFilterValueMutableLiveData.value = filterFeatureViewState
    }

    private val savedCurrentFilterValueMutableLiveData = MutableLiveData<FilterFeatureViewState>()

    fun registerFilterQueryWhenSubmitButtonClicked(query: String) {
        propertyRepository.registerFilterQueryWhenSubmitButtonClicked(query)
    }

     fun getSavedCurrentFilter(): FilterFeatureViewState {
        return savedCurrentFilterValueMutableLiveData.value!!
    }

    fun deleteCurrentFilter() {
        propertyRepository.deleteCurrentFilter()
    }

    fun getNbrOfPropertyWithThisQuery(query: String): LiveData<Int> {
        return propertyRepository.getAllPropertyFilter(query).map {
            it.size
        }.asLiveData()
    }

    fun updateFilterPrice(minPriceSelected: Int, maxPriceSelected: Int) {
        registerCurrentFilterValue(
            getSavedCurrentFilter().copy(
                minPriceSelected = minPriceSelected, maxPriceSelected = maxPriceSelected
            )
        )
    }

    fun updateFilterSurface(minSurfaceSelected: Int, maxSurfaceSelected: Int) {
        registerCurrentFilterValue(
            getSavedCurrentFilter().copy(
                minSurfaceSelected = minSurfaceSelected, maxSurfaceSelected = maxSurfaceSelected
            )
        )
    }

    fun updateFilterAgent(agentName: String) {
        if (agentName == "") {
            registerCurrentFilterValue(
                getSavedCurrentFilter().copy(
                    agentNameSelected = ""
                )
            )
        } else {
            registerCurrentFilterValue(
                getSavedCurrentFilter().copy(
                    agentNameSelected = agentName
                )
            )
        }
    }

    fun updateFilterListType(listOfTypeSelected: List<String>) {
        if (listOfTypeSelected.isNotEmpty()) {
            registerCurrentFilterValue(
                getSavedCurrentFilter().copy(
                    listOfTypeSelected = listOfTypeSelected
                )
            )
        } else {
            registerCurrentFilterValue(
                getSavedCurrentFilter().copy(
                    listOfTypeSelected = listOf()
                )
            )
        }
    }

    fun updateFilterListTown(listOfTownSelected: List<String>) {
        if (listOfTownSelected.isNotEmpty()) {
            registerCurrentFilterValue(
                getSavedCurrentFilter().copy(
                    listOfTownSelected = listOfTownSelected
                )
            )
        } else {
            registerCurrentFilterValue(
                getSavedCurrentFilter().copy(
                    listOfTownSelected = listOf()
                )
            )
        }
    }

    fun toQuery(filterFeatureViewState: FilterFeatureViewState): String {
        val typeStringBuilder = java.lang.StringBuilder()
        val townStringBuilder = java.lang.StringBuilder()

        val priceQuery =
            "SELECT * FROM PropertyEntity WHERE (price BETWEEN ${filterFeatureViewState.minPriceSelected} AND ${filterFeatureViewState.maxPriceSelected})"
        val surfaceQuery =
            " AND (surface BETWEEN ${filterFeatureViewState.minSurfaceSelected} AND ${filterFeatureViewState.maxSurfaceSelected})"
        val agentNameQuery =
            if (filterFeatureViewState.agentNameSelected == "") "" else " AND (agentName = '${filterFeatureViewState.agentNameSelected}')"
        val listOfTypeQuery = if (filterFeatureViewState.listOfTypeSelected.isEmpty()) {
            ""
        } else {
            typeStringBuilder.append(" And (type in (")
            filterFeatureViewState.listOfTypeSelected.forEach {
                typeStringBuilder.append("'$it'")
                if (filterFeatureViewState.listOfTypeSelected.indexOf(it) + 1 < filterFeatureViewState.listOfTypeSelected.size) {
                    typeStringBuilder.append(", ")
                } else {
                    typeStringBuilder.append("))")
                }
            }
            typeStringBuilder.toString()
        }
        val listOfTownQuery = if (filterFeatureViewState.listOfTownSelected.isEmpty()) {
            ""
        } else {
            townStringBuilder.append(" And (town in (")
            filterFeatureViewState.listOfTownSelected.forEach {
                townStringBuilder.append("'$it'")
                if (filterFeatureViewState.listOfTownSelected.indexOf(it) + 1 < filterFeatureViewState.listOfTownSelected.size) {
                    townStringBuilder.append(", ")
                } else {
                    townStringBuilder.append("))")
                }
            }
            townStringBuilder.toString()
        }
        return priceQuery + surfaceQuery + agentNameQuery + listOfTypeQuery + listOfTownQuery
    }


}


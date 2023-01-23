package com.openclassrooms.realestatemanager.ui.filter

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.data.models.CurrentFilterValue
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class FilterViewModel  @Inject constructor(
    private val propertyRepository: PropertyRepository
) : ViewModel() {

    val filterFeatureViewStateLiveData: LiveData<FilterFeatureViewState> =
        propertyRepository.getListIdProperty().switchMap {
            combine(
                propertyRepository.getMinMaxPriceAndSurface(it),
                propertyRepository.getAllAgent(),
                propertyRepository.getListType(),
                propertyRepository.getListTown(),
                propertyRepository.currentFilterValue,
            ) { minMaxPriceSurface, listAgent, listType, listTown, currentFilterValue ->
                FilterFeatureViewState(
                    minPrice = minMaxPriceSurface.minPrice,
                    maxPrice = minMaxPriceSurface.maxPrice,
                    minSurface = minMaxPriceSurface.minSurface,
                    maxSurface = minMaxPriceSurface.maxSurface,
                    listAgent = listAgent,
                    listOfType = listType,
                    listOfTown = listTown,
                    minPriceSelected = if(currentFilterValue.minPriceSelected != null && currentFilterValue.minPriceSelected >= minMaxPriceSurface.minPrice)
                        currentFilterValue.minPriceSelected else  minMaxPriceSurface.minPrice,
                    maxPriceSelected = if(currentFilterValue.maxPriceSelected != null && currentFilterValue.maxPriceSelected <= minMaxPriceSurface.maxPrice)
                        currentFilterValue.maxPriceSelected else minMaxPriceSurface.maxPrice,
                    minSurfaceSelected = if(currentFilterValue.minSurfaceSelected != null && currentFilterValue.minSurfaceSelected >= minMaxPriceSurface.minSurface)
                        currentFilterValue.minSurfaceSelected else  minMaxPriceSurface.minSurface,
                    maxSurfaceSelected = if(currentFilterValue.maxSurfaceSelected != null && currentFilterValue.maxSurfaceSelected <= minMaxPriceSurface.maxSurface)
                        currentFilterValue.maxSurfaceSelected else  minMaxPriceSurface.maxSurface,
                    agentNameSelected = currentFilterValue.agentNameSelected,
                    listOfTypeSelected = currentFilterValue.listOfTypeSelected,
                    listOfTownSelected = currentFilterValue.listOfTownSelected,
                )
            }.asLiveData()
        }

    fun registerCurrentFilterValueAndQuery(filterFeatureViewState: FilterFeatureViewState) {
        val query = toQuery(filterFeatureViewState)
        val currentFilterValue = CurrentFilterValue(
        minPriceSelected = filterFeatureViewState.minPriceSelected,
        maxPriceSelected = filterFeatureViewState.maxPriceSelected,
        minSurfaceSelected = filterFeatureViewState.minSurfaceSelected,
        maxSurfaceSelected = filterFeatureViewState.maxSurfaceSelected,
        agentNameSelected = filterFeatureViewState.agentNameSelected,
        listOfTypeSelected = filterFeatureViewState.listOfTypeSelected,
        listOfTownSelected = filterFeatureViewState.listOfTownSelected,
        )
        propertyRepository.registerCurrentFilterValueAndQuery(query, currentFilterValue)
        savedCurrentFilterValueMutableLiveData.value = filterFeatureViewState
    }

    private val savedCurrentFilterValueMutableLiveData = MutableLiveData<FilterFeatureViewState>()

    fun registerFilterQueryWhenSubmitButtonClicked(query: String) {
        propertyRepository.registerFilterQueryWhenSubmitButtonClicked(query)
    }

    private fun getSavedCurrentFilter(): FilterFeatureViewState {
        return savedCurrentFilterValueMutableLiveData.value!!
    }

    fun deleteCurrentFilter() {
        propertyRepository.deleteCurrentFilter()
    }

    fun getNbrOfPropertyWithThisQuery(query: String): LiveData<List<PropertyEntity>> {
       return propertyRepository.getAllPropertyFilter(query).asLiveData()
    }

    fun updateQueryFilterPrice(minPriceSelected: Int, maxPriceSelected: Int) {
        registerCurrentFilterValueAndQuery(
            getSavedCurrentFilter().copy(
                minPriceSelected = minPriceSelected,
                maxPriceSelected = maxPriceSelected
            )
        )
    }

    fun updateQueryFilterSurface(minSurfaceSelected: Int, maxSurfaceSelected: Int) {
        registerCurrentFilterValueAndQuery(
            getSavedCurrentFilter().copy(
                minSurfaceSelected = minSurfaceSelected,
                maxSurfaceSelected = maxSurfaceSelected
            )
        )
    }

    fun updateQueryFilterAgent(agentName: String) {
        if(agentName == "") {
            registerCurrentFilterValueAndQuery(
                getSavedCurrentFilter().copy(
                    agentNameSelected = ""
                )
            )
        } else {
            registerCurrentFilterValueAndQuery(
                getSavedCurrentFilter().copy(
                    agentNameSelected = agentName
                )
            )
        }
    }

    fun updateQueryFilterListType(listOfTypeSelected: List<String>) {
        if(listOfTypeSelected.isNotEmpty()) {
            registerCurrentFilterValueAndQuery(
                getSavedCurrentFilter().copy(
                    listOfTypeSelected = listOfTypeSelected
                )
            )
        } else {
            registerCurrentFilterValueAndQuery(
                getSavedCurrentFilter().copy(
                    listOfTypeSelected = listOf()
                )
            )
        }
    }
    fun updateQueryFilterListTown(listOfTownSelected: List<String>) {
        if(listOfTownSelected.isNotEmpty()) {
            registerCurrentFilterValueAndQuery(
                getSavedCurrentFilter().copy(
                    listOfTownSelected = listOfTownSelected
                )
            )
        } else {
            registerCurrentFilterValueAndQuery(
                getSavedCurrentFilter().copy(
                    listOfTownSelected = listOf()
                )
            )
        }
    }

    fun  toQuery(filterFeatureViewState: FilterFeatureViewState): String {
        val typeStringBuilder = java.lang.StringBuilder()
        val townStringBuilder = java.lang.StringBuilder()

        val priceQuery: String = "SELECT * FROM PropertyEntity WHERE (price BETWEEN ${filterFeatureViewState.minPriceSelected} AND ${filterFeatureViewState.maxPriceSelected})"
        val surfaceQuery: String = " AND (surface BETWEEN ${filterFeatureViewState.minSurfaceSelected} AND ${filterFeatureViewState.maxSurfaceSelected})"
        val agentNameQuery = if(filterFeatureViewState.agentNameSelected == "") "" else " AND (agentName = '${filterFeatureViewState.agentNameSelected}')"
        val listOfTypeQuery = if(filterFeatureViewState.listOfTypeSelected.isEmpty()) {
            ""
        } else {
            typeStringBuilder.append(" And (type in (")
            filterFeatureViewState.listOfTypeSelected.forEach {
                typeStringBuilder.append("'$it'")
                if(filterFeatureViewState.listOfTypeSelected.indexOf(it) + 1 < filterFeatureViewState.listOfTypeSelected.size) {
                    typeStringBuilder.append(", ")
                } else {
                    typeStringBuilder.append("))")
                }
            }
            typeStringBuilder.toString()
        }
        val listOfTownQuery = if(filterFeatureViewState.listOfTownSelected.isEmpty()) {
            ""
        } else {
            townStringBuilder.append(" And (town in (")
            filterFeatureViewState.listOfTownSelected.forEach {
                townStringBuilder.append("'$it'")
                if(filterFeatureViewState.listOfTownSelected.indexOf(it) + 1 < filterFeatureViewState.listOfTownSelected.size) {
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


package com.openclassrooms.realestatemanager.ui.filter

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class FilterViewModel  @Inject constructor(
    private val propertyRepository: PropertyRepository
) : ViewModel() {

    val filterViewStateLiveData: LiveData<FilterViewState> =
        propertyRepository.getListIdProperty().switchMap {
            combine(
                propertyRepository.getMinMaxPriceAndSurface(it),
                propertyRepository.getAllAgent(),
                propertyRepository.getListType(),
                propertyRepository.getListTown(),
            ) { minMaxPriceSurface, listAgent, listType, listTown ->
                FilterViewState(
                    minPrice = minMaxPriceSurface.minPrice,
                    maxPrice = minMaxPriceSurface.maxPrice,
                    minSurface = minMaxPriceSurface.minSurface,
                    maxSurface = minMaxPriceSurface.maxSurface,
                    listAgent = listAgent,
                    listOfType = listType,
                    listOfTown = listTown,
                )
            }.asLiveData()
        }

    fun getNbrOfPropertyWithThisQuery(query: String): LiveData<List<PropertyEntity>> {
       return propertyRepository.getAllPropertyFilter(query).asLiveData()
    }

    fun setInitialQueryFilter(filterViewState: FilterViewState) {
        queryFilterMutableLiveData.value = QueryFilterViewState(
            price = "SELECT * FROM PropertyEntity WHERE (price BETWEEN ${filterViewState.minPrice} AND ${filterViewState.maxPrice})",
            surface = " AND (surface BETWEEN ${filterViewState.minSurface} AND ${filterViewState.maxSurface})",
            agentName = "",
            listOfType = "",
            listOfTown = ""
        )
    }

    private val initialQueryFilter =
        QueryFilterViewState(
            price = "",
            surface = "",
            agentName = "",
            listOfType = "",
            listOfTown = ""
        )

    private val queryFilterMutableLiveData: MutableLiveData<QueryFilterViewState> = MutableLiveData(initialQueryFilter)

    val queryFilterLiveData: LiveData<QueryFilterViewState> = queryFilterMutableLiveData

    private fun getQueryFilter(): QueryFilterViewState {
        return queryFilterMutableLiveData.value!!
    }

    private fun updateQuery(query: QueryFilterViewState) {
        queryFilterMutableLiveData.value = query
    }

    fun updateQueryFilterPrice(minPrice: Int, maxPrice: Int) {
        updateQuery(
            getQueryFilter().copy(
                price = "SELECT * FROM PropertyEntity WHERE (price BETWEEN $minPrice AND $maxPrice) "
            )
        )
    }

    fun updateQueryFilterSurface(minSurface: Int, maxSurface: Int) {
        updateQuery(
            getQueryFilter().copy(
                surface = "AND (surface BETWEEN $minSurface AND $maxSurface) "
            )
        )
    }

    fun updateQueryFilterAgent(agentName: String) {
        if(agentName == "") {
            updateQuery(
                getQueryFilter().copy(
                    agentName = ""
                )
            )
        } else {
            updateQuery(
                getQueryFilter().copy(
                    agentName = "AND (agentName = '$agentName') "
                )
            )
        }
    }

    fun updateQueryFilterListType(typeSelected: List<String>) {
        val stringBuilderQueryType: StringBuilder = java.lang.StringBuilder()
        if(typeSelected.isNotEmpty()) {
            typeSelected.forEach {
                stringBuilderQueryType.append("'$it'")
                if(typeSelected.indexOf(it) + 1 < typeSelected.size) {
                    stringBuilderQueryType.append(", ")
                }
            }
            updateQuery(
                getQueryFilter().copy(
                    listOfType = "AND (type IN ($stringBuilderQueryType)) "
                )
            )
        } else {
            updateQuery(
                getQueryFilter().copy(
                    listOfType = ""
                )
            )
        }
    }
    fun updateQueryFilterListTown(townSelected: List<String>) {
        val stringBuilderQueryTown: StringBuilder = java.lang.StringBuilder()
        if(townSelected.isNotEmpty()) {
            townSelected.forEach {
                stringBuilderQueryTown.append("'$it'")
                if(townSelected.indexOf(it) + 1 < townSelected.size) {
                    stringBuilderQueryTown.append(", ")
                }
            }
            updateQuery(
                getQueryFilter().copy(
                    listOfType = "AND (town IN ($stringBuilderQueryTown)) "
                )
            )
        } else {
            updateQuery(
                getQueryFilter().copy(
                    listOfType = ""
                )
            )
        }
    }

    fun registerCurrentSearch(currentQuery : QueryFilterViewState) {

    }

}


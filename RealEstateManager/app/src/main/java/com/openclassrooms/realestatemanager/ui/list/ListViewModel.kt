package com.openclassrooms.realestatemanager.ui.list


import androidx.lifecycle.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.data.repositories.CurrentPropertyRepository
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ListViewModel  @Inject constructor(
    //
    private val propertyRepository: PropertyRepository,
    private val currentPropertyRepository: CurrentPropertyRepository
) : ViewModel(){

    val propertyListFilterLiveData: LiveData<List<ListViewState>> =
        propertyRepository.queryFilterListFragmentLiveData.switchMap { query ->
            propertyRepository.getAllPropertyFilter(query).map { listFilterProperty ->
                listFilterProperty.map {
                    ListViewState(
                        it.type,
                        it.town,
                        it.price,
                        it.mainPicture,
                        it.id,
                        it.agentName,
                        it.entryDate,
                        it.dateOfSale
                    )
                }
            }.asLiveData()
        }

    fun onItemClicked(id: Long) {
        currentPropertyRepository.setCurrentId(id)
        propertyRepository.setCurrentPropertyId(id)
    }


}
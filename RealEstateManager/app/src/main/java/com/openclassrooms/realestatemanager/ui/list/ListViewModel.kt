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
    //Nous récupérons la liste de Property dans notre fakePropertyRepository et nous la transformons en liste de ListViewState object
    val propertyListLiveData: LiveData<List<ListViewState>> = propertyRepository.getAllProperty.map { properties ->
        properties.map {
            ListViewState(
                it.type,
                it.town,
                it.price,
                it.mainPicture,
                it.id,
                it.agentName,
            )
        }
    }.asLiveData()



    val propertyListFilterLiveData: LiveData<List<ListViewState>> =
        propertyRepository.queryFilterLiveData.switchMap { query ->
            propertyRepository.getAllPropertyFilter(query).map { listFilterProperty ->
                listFilterProperty.map {
                    ListViewState(
                        it.type,
                        it.town,
                        it.price,
                        it.mainPicture,
                        it.id,
                        it.agentName,
                    )
                }
            }.asLiveData()
        }


    fun onItemClicked(id: Long) {
        currentPropertyRepository.setCurrentId(id)
    }

//    fun onItemClickedFlow(id: String) {
//        currentPropertyRepository.setCurrentIdFlow(id)
//    }

}
package com.openclassrooms.realestatemanager.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.openclassrooms.realestatemanager.data.current_property.CurrentPropertyRepository
import com.openclassrooms.realestatemanager.data.property.FakePropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListViewModel  @Inject constructor(
    //
    fakePropertyRepository: FakePropertyRepository,
    private val currentPropertyRepository: CurrentPropertyRepository
) : ViewModel(){
    //Nous récupérons la liste de Property dans notre fakePropertyRepository et nous la transformons en liste de ListViewState object
    val propertyListLiveData: LiveData<List<ListViewState>> = fakePropertyRepository.getListPropertyLiveData().map { properties ->
        properties.map {
            ListViewState(
                it.id,
                it.type,
                it.district,
                it.price,
                it.mainPicture
            )
        }
    }


    fun onItemClicked(id: String) {
        currentPropertyRepository.setCurrentId(id)
    }

}
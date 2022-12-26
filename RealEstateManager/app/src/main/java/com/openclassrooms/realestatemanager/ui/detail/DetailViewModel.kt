package com.openclassrooms.realestatemanager.ui.detail


import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import com.openclassrooms.realestatemanager.data.repositories.CurrentPropertyRepository
import com.openclassrooms.realestatemanager.data.models.entities.PropertyPicturesEntity
import com.openclassrooms.realestatemanager.data.repositories.FormPropertyRepository
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val currentPropertyRepository: CurrentPropertyRepository,
    private val formPropertyRepository: FormPropertyRepository
) : ViewModel() {
    // On récupère l'id stocké dans le currentPropertyRepository et avec switchMap nous mettons l'id récupéré en parametre de la getPropertyByIdLiveData méthode
    //qui récupéra un objet Property correspondant à l'id que nous transformerons ensuite en DetailViewState object grâce au .map

    val detailLiveData : LiveData<DetailViewState> =
        currentPropertyRepository.currentIdLiveData.switchMap { id ->
            if(id != 0L) {
                propertyRepository.getPropertyByIdFlow(id).map {
                    DetailViewState.Selected(
                        it.id,
                        it.district,
                        it.description,
                        it.town,
                        it.address,
                        it.postalCode,
                        it.surface,
                        it.numberOfRooms,
                        it.numberOfBathrooms,
                        it.numberOfBedrooms,
                        it.state
                    )
                }.asLiveData()
            } else {
                //On créer un live data qu'on retourne en appliquant en attribuant une valeur qui est DetailViewState.Empty
                MutableLiveData<DetailViewState>().apply {
                    value = DetailViewState.Empty
                }
            }
        }


    val listPictureLiveData : LiveData<List<PropertyPicturesEntity>> =
        currentPropertyRepository.currentIdLiveData.switchMap { id ->
            propertyRepository.getAllPicturesOfThisProperty(id).map {
                    it
            }.asLiveData()
        }


    val detailLiveDataDefault: LiveData<DetailViewState> =
        propertyRepository.getPropertyByIdFlow(1).map {
        DetailViewState.Selected(
            it.id,
            it.district,
            it.description,
            it.town,
            it.address,
            it.postalCode,
            it.surface,
            it.numberOfRooms,
            it.numberOfBathrooms,
            it.numberOfBedrooms,
            it.state
        )
    }.asLiveData()

    val listPictureLiveDataDefault: LiveData<List<PropertyPicturesEntity>> =
        propertyRepository.getAllPicturesOfThisProperty(1).map {
            it
    }.asLiveData()

    val propertyToUpdateLiveData: LiveData<PropertyEntity> =
        currentPropertyRepository.currentIdLiveData.switchMap { id ->
            propertyRepository.getPropertyByIdFlow(id).map {
                it
            }.asLiveData()
        }

    fun setFormUpdate(propertyToUpdate: PropertyEntity) {
        formPropertyRepository.setFormUpdate(propertyToUpdate)
//        formPropertyRepository.setFormProperty(propertyToUpdate)
    }



    fun setFormPropertyIdUpdate(id: Long) {
        propertyRepository.setCurrentId(id)
    }





}
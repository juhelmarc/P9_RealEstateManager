package com.openclassrooms.realestatemanager.ui.detail


import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.data.PoiList
import com.openclassrooms.realestatemanager.data.repositories.CurrentPropertyRepository
import com.openclassrooms.realestatemanager.data.models.entities.PropertyPictureEntity
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val currentPropertyRepository: CurrentPropertyRepository,
) : ViewModel() {
    // On récupère l'id stocké dans le currentPropertyRepository et avec switchMap nous mettons l'id récupéré en parametre de la getPropertyByIdLiveData méthode
    //qui récupéra un objet Property correspondant à l'id que nous transformerons ensuite en DetailViewState object grâce au .map

    val detailLiveData : LiveData<DetailViewState> =
        currentPropertyRepository.currentIdLiveData.switchMap { id ->
            propertyRepository.setCurrentPropertyId(id)
            if(id != 0L) {
                propertyRepository.getPropertyByIdFlow(id).map {
                    val poiIdListOfThisProperty: List<Int> = it.poiSelected

                    val listPoiSelectedOrNot: List<ChipPoiViewStateDetail> =
                        PoiList.values().map { poi ->
                            ChipPoiViewStateDetail(
                                poiId = poi.poiId,
                                isSelected = poiIdListOfThisProperty.contains(poi.poiId)
                            )
                        }
                    DetailViewState.Selected(
                        it.id,
                        it.description,
                        it.town,
                        it.address,
                        it.postalCode,
                        it.surface,
                        it.numberOfRooms,
                        it.numberOfBathrooms,
                        it.numberOfBedrooms,
                        it.state,
                        listPoiSelectedOrNot
                    )
                }.asLiveData()
            } else {
                //On créer un live data qu'on retourne en appliquant en attribuant une valeur qui est DetailViewState.Empty
                MutableLiveData<DetailViewState>().apply {
                    value = DetailViewState.Empty
                }
            }
        }


    val listPictureLiveData : LiveData<List<PropertyPictureEntity>> =
        currentPropertyRepository.currentIdLiveData.switchMap { id ->
            propertyRepository.getAllPicturesOfThisProperty(id).map {
                    it
            }.asLiveData()
        }


    val detailLiveDataDefault: LiveData<DetailViewState> =
        propertyRepository.getPropertyByIdFlow(1).map {
            val poiIdListOfThisProperty: List<Int> = it.poiSelected

            val listPoiSelectedOrNot: List<ChipPoiViewStateDetail> =
                PoiList.values().map { poi ->
                    ChipPoiViewStateDetail(
                        poiId = poi.poiId,
                        isSelected = poiIdListOfThisProperty.contains(poi.poiId)
                    )
                }
        DetailViewState.Selected(
            it.id,
            it.description,
            it.town,
            it.address,
            it.postalCode,
            it.surface,
            it.numberOfRooms,
            it.numberOfBathrooms,
            it.numberOfBedrooms,
            it.state,
            listPoiSelectedOrNot
        )
    }.asLiveData()

    val listPictureLiveDataDefault: LiveData<List<PropertyPictureEntity>> =
        propertyRepository.getAllPicturesOfThisProperty(1).map {
            it
    }.asLiveData()

    fun setFormPropertyIdUpdate() {
        propertyRepository.setCurrentPropertyId(-1L)
    }





}
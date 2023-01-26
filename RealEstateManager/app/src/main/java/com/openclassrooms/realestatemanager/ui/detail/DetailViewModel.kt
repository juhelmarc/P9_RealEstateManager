package com.openclassrooms.realestatemanager.ui.detail


import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.data.PoiList
import com.openclassrooms.realestatemanager.data.repositories.CurrentPropertyRepository
import com.openclassrooms.realestatemanager.data.models.entities.PropertyPictureEntity
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val currentPropertyRepository: CurrentPropertyRepository,
) : ViewModel() {
    // On récupère l'id stocké dans le currentPropertyRepository et avec switchMap nous mettons l'id récupéré en parametre de la getPropertyByIdLiveData méthode
    //qui récupéra un objet Property correspondant à l'id que nous transformerons ensuite en DetailViewState object grâce au .map

        val detailLiveData : LiveData<DetailViewState> =
            propertyRepository.currentIdLiveData.switchMap { id ->
                if (id != 0L) {
                    combine(
                        propertyRepository.getPropertyByIdFlow(id),
                        propertyRepository.getAllPicturesOfThisProperty(id)
                    ) { it, listPicture ->
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
                            listPoiSelectedOrNot,
                            listPicture,
                            it.entryDate,
                            it.dateOfSale
                        )
                    }.asLiveData()
                } else {
                    combine(
                        propertyRepository.getAllProperty,
                    ) { listProperty ->
                        if (listProperty[0].isEmpty()) {
                            DetailViewState.Empty

                        } else {
                            propertyRepository.setCurrentPropertyId(1L)
                            DetailViewState.Empty
                        }
                    }.asLiveData()
                }
            }

        fun setIsAnUpdate(isAnUpdate: Boolean) {
            propertyRepository.setIsAnUpdateProperty(isAnUpdate)
        }







//    val testLiveDataViewState : LiveData<DetailViewState> =
//        currentPropertyRepository.currentIdLiveData.switchMap { id ->
//            if(id == 0L) {
//                combine(
//                    propertyRepository.getAllProperty,
//                    propertyRepository.getPropertyByIdFlow(1L)
//                ) {
//                        listProperty, defaultProperty ->
//                    if(listProperty.isNotEmpty()) {
//                        val poiIdListOfThisProperty: List<Int> = defaultProperty.poiSelected
//                        val listPoiSelectedOrNot: List<ChipPoiViewStateDetail> =
//                            PoiList.values().map { poi ->
//                                ChipPoiViewStateDetail(
//                                    poiId = poi.poiId,
//                                    isSelected = poiIdListOfThisProperty.contains(poi.poiId)
//                                )
//                            }
//                        DetailViewState.NotSelected(
//                            defaultProperty.id,
//                            defaultProperty.description,
//                            defaultProperty.town,
//                            defaultProperty.address,
//                            defaultProperty.postalCode,
//                            defaultProperty.surface,
//                            defaultProperty.numberOfRooms,
//                            defaultProperty.numberOfBathrooms,
//                            defaultProperty.numberOfBedrooms,
//                            defaultProperty.state,
//                            listPoiSelectedOrNot
//                        )
//                    } else {
//                        DetailViewState.Empty
//                    }
//                }.asLiveData()
//            } else {
//                propertyRepository.getPropertyByIdFlow(id).map {
//                    val poiIdListOfThisProperty: List<Int> = it.poiSelected
//                    val listPoiSelectedOrNot: List<ChipPoiViewStateDetail> =
//                        PoiList.values().map { poi ->
//                            ChipPoiViewStateDetail(
//                                poiId = poi.poiId,
//                                isSelected = poiIdListOfThisProperty.contains(poi.poiId)
//                            )
//                        }
//                    DetailViewState.Selected(
//                        it.id,
//                        it.description,
//                        it.town,
//                        it.address,
//                        it.postalCode,
//                        it.surface,
//                        it.numberOfRooms,
//                        it.numberOfBathrooms,
//                        it.numberOfBedrooms,
//                        it.state,
//                        listPoiSelectedOrNot
//                    )
//                }.asLiveData()
//            }
//        }


}
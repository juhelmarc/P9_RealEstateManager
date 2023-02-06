package com.openclassrooms.realestatemanager.ui.detail


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.openclassrooms.realestatemanager.data.PoiList
import com.openclassrooms.realestatemanager.data.repositories.CurrentPropertyRepository
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val currentPropertyRepository: CurrentPropertyRepository,
) : ViewModel() {

    val detailLiveData: LiveData<DetailViewState> =
        propertyRepository.getCurrentIdLiveData().switchMap { id ->
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
                    propertyRepository.getAllProperty(),
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
}
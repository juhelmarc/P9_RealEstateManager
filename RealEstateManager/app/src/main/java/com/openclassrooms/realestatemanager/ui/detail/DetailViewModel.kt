package com.openclassrooms.realestatemanager.ui.detail


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.denzcoskun.imageslider.models.SlideModel
import com.openclassrooms.realestatemanager.data.current_property.CurrentPropertyRepository
import com.openclassrooms.realestatemanager.data.property.FakePropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    fakePropertyRepository: FakePropertyRepository,
    private val currentPropertyRepository: CurrentPropertyRepository
) : ViewModel() {
    // On récupère l'id stocké dans le currentPropertyRepository et avec switchMap nous mettons l'id récupéré en parametre de la getPropertyByIdLiveData méthode
    //qui récupéra un objet Property correspondant à l'id que nous transformerons ensuite en DetailViewState object grâce au .map
    val detailLiveData : LiveData<DetailViewState> =
        currentPropertyRepository.currentIdLiveData.switchMap { id ->
            fakePropertyRepository.getPropertyByIdLiveData(id).map {
                DetailViewState(
                    it.id,
                    it.district,
                    it.listPicture,
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
            }
        }
    val listPictureLiveData : LiveData<List<SlideModel>> =
        currentPropertyRepository.currentIdLiveData.switchMap { id ->
            fakePropertyRepository.getPropertyByIdLiveData(id).map {
                ArrayList<SlideModel>(
                    it.listPicture
                )
            }
        }
    fun setPictureClicked(imageUrl : String) {
        currentPropertyRepository.setPictureClicked(imageUrl)
    }


}
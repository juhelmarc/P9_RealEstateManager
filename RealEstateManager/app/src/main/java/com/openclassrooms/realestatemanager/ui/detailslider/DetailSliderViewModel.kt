package com.openclassrooms.realestatemanager.ui.detailslider

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.denzcoskun.imageslider.models.SlideModel
import com.openclassrooms.realestatemanager.data.current_property.CurrentPropertyRepository
import com.openclassrooms.realestatemanager.data.property.FakePropertyRepository
import com.openclassrooms.realestatemanager.ui.detail.DetailViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailSliderViewModel @Inject constructor(
    fakePropertyRepository: FakePropertyRepository,
    currentPropertyRepository: CurrentPropertyRepository
) : ViewModel(){

    val detailLiveData : LiveData<DetailSliderViewState> =
        currentPropertyRepository.currentIdLiveData.switchMap { id ->
            fakePropertyRepository.getPropertyByIdLiveData(id).map {
                DetailSliderViewState(
                    it.id,
                    it.district,
                    it.listPicture
                )
            }
        }

    val pictureClickedLiveData: LiveData<String> = currentPropertyRepository.pictureClickedLiveData


}
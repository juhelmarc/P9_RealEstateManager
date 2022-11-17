package com.openclassrooms.realestatemanager.data.current_property

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentPropertyRepository @Inject constructor(){
    private val currentIdMutableLiveData = MutableLiveData<String>()
    private val pictureClickedMutableLiveData = MutableLiveData<String>()



    val currentIdLiveData: LiveData<String> = currentIdMutableLiveData
    val pictureClickedLiveData: LiveData<String> = pictureClickedMutableLiveData



    @MainThread
    fun setCurrentId(currentId: String) {
        currentIdMutableLiveData.value = currentId
    }

    @MainThread
    fun setPictureClicked(imageUrl: String) {
        pictureClickedMutableLiveData.value = imageUrl
    }


}
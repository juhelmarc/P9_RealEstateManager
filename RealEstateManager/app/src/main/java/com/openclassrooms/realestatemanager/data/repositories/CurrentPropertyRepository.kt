package com.openclassrooms.realestatemanager.data.repositories

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentPropertyRepository @Inject constructor(){
    private val currentIdMutableLiveData = MutableLiveData<Long>()

    val currentIdLiveData: LiveData<Long> = currentIdMutableLiveData

    @MainThread
    fun setCurrentId(currentId: Long) {
        currentIdMutableLiveData.value = currentId
    }








}
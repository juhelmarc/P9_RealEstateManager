package com.openclassrooms.realestatemanager.ui.detail

import com.denzcoskun.imageslider.models.SlideModel


data class DetailViewState (
    val id : String,
    val district : String,
    val listPicture : ArrayList<SlideModel>,
    val description: String,
    val town: String,
    val address: String,
    val postalCode: String,
    val surface: Int,
    val numberOfRooms: Int,
    val numberOfBathrooms: Int,
    val numberOfBedRooms: Int,
    val state: String,
        )


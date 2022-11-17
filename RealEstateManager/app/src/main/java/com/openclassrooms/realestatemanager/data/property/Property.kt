package com.openclassrooms.realestatemanager.data.property

import com.denzcoskun.imageslider.models.SlideModel

class Property(
    val id: String,
    val type: String,
    val district: String,
    val price: Int,
    val description: String,
    val surface: Int,
    val numberOfRooms: Int,
    val numberOfBathrooms: Int,
    val numberOfBedrooms: Int,
    val town: String,
    val address: String,
    val postalCode: String,
    val state: String,
    val mainPicture: String,
    val listPicture: ArrayList<SlideModel>
)
package com.openclassrooms.realestatemanager.ui.detailslider

import com.denzcoskun.imageslider.models.SlideModel

data class DetailSliderViewState (
    val id : String,
    val district : String,
    val listPicture : ArrayList<SlideModel>,
)
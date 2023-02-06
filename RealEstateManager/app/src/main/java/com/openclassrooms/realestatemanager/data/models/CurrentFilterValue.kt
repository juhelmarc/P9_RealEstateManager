package com.openclassrooms.realestatemanager.data.models

data class CurrentFilterValue(
    val minPriceSelected: Int?,
    val maxPriceSelected: Int?,
    val minSurfaceSelected: Int?,
    val maxSurfaceSelected: Int?,
    val agentNameSelected: String?,
    val listOfTypeSelected: List<String>,
    val listOfTownSelected: List<String>,
)

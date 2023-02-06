package com.openclassrooms.realestatemanager.ui.filter

import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity

data class FilterFeatureViewState(
    val minPrice: Int,
    val maxPrice: Int,
    val minSurface: Int,
    val maxSurface: Int,
    val listAgent: List<AgentEntity>,
    val listOfType: List<String>,
    val listOfTown: List<String>,
    val minPriceSelected: Int?,
    val maxPriceSelected: Int?,
    val minSurfaceSelected: Int?,
    val maxSurfaceSelected: Int?,
    val agentNameSelected: String?,
    val listOfTypeSelected: List<String>,
    val listOfTownSelected: List<String>,
)


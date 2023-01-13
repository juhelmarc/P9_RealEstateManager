package com.openclassrooms.realestatemanager.ui.filter

import com.openclassrooms.realestatemanager.data.PoiList
import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity

data class FilterViewState (
    val minPrice: Int?,
    val maxPrice: Int?,
    val minSurface: Int?,
    val maxSurface: Int?,
    val listAgent: List<AgentEntity>?,
    val listOfType: List<String>?,
    val listOfTown: List<String>?,
        )

package com.openclassrooms.realestatemanager.ui.list

import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity

data class ListViewState (
    val type: String?,
    val district: String?,
    val price: String?,
    val mainPicture: String?,
    val id : Long,
    val agentName: String,
)




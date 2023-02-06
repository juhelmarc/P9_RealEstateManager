package com.openclassrooms.realestatemanager.ui.list

data class ListViewState (
    val type: String?,
    val town: String?,
    val price: Int?,
    val mainPicture: String?,
    val id : Long,
    val agentName: String,
    val entryDate: String?,
    val dateOfSale: String?
)




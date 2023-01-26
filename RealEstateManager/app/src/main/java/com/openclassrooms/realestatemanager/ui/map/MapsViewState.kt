package com.openclassrooms.realestatemanager.ui.map

data class MapsViewState (
        val id: Long,
        val lat: Double?,
        val lng : Double?,
        val price: Int?,
        val picture: String?,
        val town: String?,
        val type: String?,
        )

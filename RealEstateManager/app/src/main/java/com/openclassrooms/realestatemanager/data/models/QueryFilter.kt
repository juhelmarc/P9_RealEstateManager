package com.openclassrooms.realestatemanager.data.models


data class QueryFilter (
    val price: String?,
    val surface: String?,
    val agentName: String?,
    val listOfType: String?,
    val listOfTown: String?
) {
    override fun toString() = price + surface + agentName + listOfType + listOfTown
}

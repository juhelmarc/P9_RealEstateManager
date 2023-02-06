package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity

class PropertyFixtures {

    object PropertyEntityUtils {
        fun create() : PropertyEntity {
            return PropertyEntity(
                agentId = AgentFixtures.AgentListUtils.create()[0].agentId,
                agentName = AgentFixtures.AgentListUtils.create()[0].name,
                type = null,
                price = null,
                description = null,
                surface = null,
                numberOfRooms = null,
                numberOfBathrooms = null,
                numberOfBedrooms = null,
                town = null,
                address = null,
                postalCode = null,
                state = null,
                mainPicture = null,
                isAvailable = true,
                entryDate = null,
                dateOfSale = null,
                poiSelected = listOf(),
                lat = null,
                lng = null,
            )
        }
    }
}
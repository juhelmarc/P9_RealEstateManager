package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.data.PoiList
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import com.openclassrooms.realestatemanager.ui.detail.ChipPoiViewStateDetail
import com.openclassrooms.realestatemanager.ui.detail.DetailViewState

class PropertyFixtures {
    private val property = PropertyEntityUtils.create()

    object PropertyEntityUtils {
        fun create() : PropertyEntity {
            return PropertyEntity(
                id = 1L,
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
    object DetailViewStateSelectedUtils {
        fun create() : DetailViewState.Selected {
            return DetailViewState.Selected (
                id = PropertyEntityUtils.create().id,
                description = PropertyEntityUtils.create().description,
                town = PropertyEntityUtils.create().town,
                address = PropertyEntityUtils.create().address,
                postalCode = PropertyEntityUtils.create().postalCode,
                surface = PropertyEntityUtils.create().surface,
                numberOfRooms = PropertyEntityUtils.create().numberOfRooms,
                numberOfBathrooms = PropertyEntityUtils.create().numberOfBathrooms,
                numberOfBedRooms = PropertyEntityUtils.create().numberOfBedrooms,
                state = PropertyEntityUtils.create().state,
                poiSelected = PoiList.values().map {
                    ChipPoiViewStateDetail(
                        poiId = it.poiId,
                        isSelected = false
                    )
                },
                listPicture = PropertyPictureFixtures.ListPictureUtils.create(),
                entryDate = PropertyEntityUtils.create().entryDate,
                sellingDate = PropertyEntityUtils.create().dateOfSale
            )
        }
    }

    object DetailViewStateEmptyUtils {
        fun create() : DetailViewState.Empty {
            return DetailViewState.Empty
        }
    }
}
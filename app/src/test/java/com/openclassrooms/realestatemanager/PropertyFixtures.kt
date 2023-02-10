package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.data.PoiList
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import com.openclassrooms.realestatemanager.ui.detail.ChipPoiViewStateDetail
import com.openclassrooms.realestatemanager.ui.detail.DetailViewState
import com.openclassrooms.realestatemanager.ui.formProperty.FormPropertyViewState
import com.openclassrooms.realestatemanager.ui.list.ListViewState
import com.openclassrooms.realestatemanager.ui.map.MapsViewState

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

    object FormPropertyViewStateUpdateUtils {
        fun create() : FormPropertyViewState {
            return FormPropertyViewState(
            id = PropertyEntityUtils.create().id,
            listPicture = PropertyPictureFixtures.ListPictureUtils.create(),
            agentId = PropertyEntityUtils.create().agentId,
            agentName = PropertyEntityUtils.create().agentName,
            type = PropertyEntityUtils.create().type,
            price = PropertyEntityUtils.create().price,
            description = PropertyEntityUtils.create().description,
            surface = PropertyEntityUtils.create().surface,
            numberOfRooms = PropertyEntityUtils.create().numberOfRooms,
            numberOfBathrooms = PropertyEntityUtils.create().numberOfBathrooms,
            numberOfBedrooms = PropertyEntityUtils.create().numberOfBedrooms,
            town = PropertyEntityUtils.create().town,
            address = PropertyEntityUtils.create().address,
            postalCode = PropertyEntityUtils.create().postalCode,
            state = PropertyEntityUtils.create().state,
            mainPicture = PropertyPictureFixtures.ListPictureUtils.create()[0].url,
            isAvailable = PropertyEntityUtils.create().isAvailable,
            entryDate = PropertyEntityUtils.create().entryDate,
            dateOfSale = PropertyEntityUtils.create().dateOfSale,
            listPoiSelectedOrNot = PoiList.values().map {
                FormPropertyViewState.ChipPoiViewState(
                    poiId = it.poiId,
                    isSelected = false
                )
            },
            lat = PropertyEntityUtils.create().lat,
            lng = PropertyEntityUtils.create().lng,
            typeError = null,
            agentError = null,
            postalCodeError = null,
            addressError = null,
            townError = null,
            entryDateError = null,
            dateOfSaleError = null,
            pictureError = null,
            latLngError = null
            )
        }
    }

    object FormPropertyViewStateEditUtils {
        fun create() : FormPropertyViewState {
           return FormPropertyViewState(
                0L,
                emptyList(),
                0L,
                "",
                "",
                0,
                "",
                0,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                true,
                "",
                "",
                PoiList.values().map { poi ->
                    FormPropertyViewState.ChipPoiViewState(
                        poiId = poi.poiId,
                        isSelected = false
                    )
                },
                null,
                null,

                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
        }
    }

    object ListViewStateUtils {
        fun create() : ListViewState {
           return ListViewState(
                type = PropertyEntityUtils.create().type,
                town = PropertyEntityUtils.create().town,
                price = PropertyEntityUtils.create().price,
                mainPicture = PropertyEntityUtils.create().mainPicture,
                id = PropertyEntityUtils.create().id,
                agentName = PropertyEntityUtils.create().agentName,
                entryDate = PropertyEntityUtils.create().entryDate,
                dateOfSale = PropertyEntityUtils.create().dateOfSale
           )
        }
    }

    object MapsViewStateUtils {
        fun create() : MapsViewState {
            return MapsViewState(
                id = PropertyEntityUtils.create().id,
                lat = PropertyEntityUtils.create().lat,
                lng = PropertyEntityUtils.create().lng,
                price = PropertyEntityUtils.create().price,
                picture = PropertyEntityUtils.create().mainPicture,
                town = PropertyEntityUtils.create().town,
                type = PropertyEntityUtils.create().type
            )
        }
    }


    object DetailViewStateEmptyUtils {
        fun create() : DetailViewState.Empty {
            return DetailViewState.Empty
        }
    }
}
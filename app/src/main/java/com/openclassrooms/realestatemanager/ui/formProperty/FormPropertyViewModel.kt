package com.openclassrooms.realestatemanager.ui.formProperty

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.data.PoiList
import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyPictureEntity
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FormPropertyViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository,
) : ViewModel() {

    private val messageError = "Is mandatory"



    fun getAgentListLiveData() : LiveData<List<AgentEntity>> {
        return propertyRepository.getAllAgent().asLiveData()
    }


    fun getIsAnUpdate() : Boolean  {
      return  propertyRepository.getIsAnUpdatePropertyLiveData().value!!
    }


    fun getInitialViewStateLiveData(): LiveData<FormPropertyViewState> {
      return  propertyRepository.getCurrentIdLiveData().switchMap { id ->
            propertyRepository.getIsAnUpdatePropertyLiveData().switchMap { isAnUpdate ->
                if (isAnUpdate) {
                    combine(
                        propertyRepository.getPropertyByIdFlow(id),
                        propertyRepository.getAllPicturesOfThisProperty(id),
                    ) { it, listPicture ->
                        val mainPicture: String =
                            if (listPicture.isNotEmpty()) listPicture[0].url else ""
                        val poiIdListOfThisProperty: List<Int> = it.poiSelected
                        val listPoiSelectedOrNot: List<FormPropertyViewState.ChipPoiViewState> =
                            PoiList.values().map { poi ->
                                FormPropertyViewState.ChipPoiViewState(
                                    poiId = poi.poiId,
                                    isSelected = poiIdListOfThisProperty.contains(poi.poiId)
                                )
                            }
                        FormPropertyViewState(
                            it.id,
                            listPicture,
                            it.agentId,
                            it.agentName,
                            it.type,
                            it.price,
                            it.description,
                            it.surface,
                            it.numberOfRooms,
                            it.numberOfBathrooms,
                            it.numberOfBedrooms,
                            it.town,
                            it.address,
                            it.postalCode,
                            it.state,
                            mainPicture,
                            it.isAvailable,
                            it.entryDate,
                            it.dateOfSale,
                            listPoiSelectedOrNot,
                            it.lat,
                            it.lng,
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
                    }.asLiveData()
                } else {
                    MutableLiveData<FormPropertyViewState>().apply {
                        value = viewStateEmpty
                    }
                }
            }
        }
    }


    private val viewStateEmpty = FormPropertyViewState(
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

    fun setInitialViewState(formPropertyViewState: FormPropertyViewState) {
        mutableStateFlowViewState.value = formPropertyViewState
    }

     fun getPropertyViewState(): FormPropertyViewState {
        return mutableStateFlowViewState.value
    }

    private fun updatePropertyViewState(formPropertyViewState: FormPropertyViewState) {
        mutableStateFlowViewState.value = formPropertyViewState
    }

    private val mutableStateFlowViewState = MutableStateFlow(viewStateEmpty)

    fun getViewStateLiveData(): LiveData<FormPropertyViewState>  {
        return mutableStateFlowViewState.asLiveData()
    }


    fun onSubmitButtonClicked(): Boolean {
        if (checkError()) {
            updatePropertyEntity(getPropertyViewState())
        }
        return checkError()
    }

    private fun checkError(): Boolean {
        if (getPropertyViewState().agentName == "") {
            updatePropertyViewState(
                getPropertyViewState().copy(agentError = "Chose an agent")
            )
            //Guard de swift return true
        } else {
            updatePropertyViewState(
                getPropertyViewState().copy(agentError = null)
            )
        }
        if (getPropertyViewState().type == "") {
            updatePropertyViewState(
                getPropertyViewState().copy(typeError = "Add a type")
            )
        } else {
            updatePropertyViewState(
                getPropertyViewState().copy(typeError = null)
            )
        }

        if (getPropertyViewState().postalCode.toString().length != 5) {
            updatePropertyViewState(
                getPropertyViewState().copy(postalCodeError = "5 numbers are required")
            )
        } else {
            updatePropertyViewState(
                getPropertyViewState().copy(postalCodeError = null)
            )
        }
        if (getPropertyViewState().address == "") {
            updatePropertyViewState(
                getPropertyViewState().copy(addressError = messageError)
            )
        } else {
            updatePropertyViewState(
                getPropertyViewState().copy(addressError = null)
            )
        }
        if (getPropertyViewState().town == "") {
            updatePropertyViewState(
                getPropertyViewState().copy(townError = "Add a Town")
            )
        } else {
            updatePropertyViewState(
                getPropertyViewState().copy(townError = null)
            )
        }
        if (getPropertyViewState().entryDate == "" && getPropertyViewState().dateOfSale != "") {
            updatePropertyViewState(
                getPropertyViewState().copy(entryDateError = "Edit entry date")
            )
        } else {
            updatePropertyViewState(
                getPropertyViewState().copy(entryDateError = null)
            )
        }
        if (getPropertyViewState().entryDate != "" && getPropertyViewState().dateOfSale != "" &&
            getPropertyViewState().entryDate!! > getPropertyViewState().dateOfSale!!
        ) {
            updatePropertyViewState(
                getPropertyViewState().copy(dateOfSaleError = "Date of sale should be after entry date")
            )
        } else {
            updatePropertyViewState(
                getPropertyViewState().copy(dateOfSaleError = null)
            )
        }
        if (getPropertyViewState().listPicture.isEmpty()) {
            updatePropertyViewState(
                getPropertyViewState().copy(pictureError = "You need add one picture")
            )
        } else {
            updatePropertyViewState(
                getPropertyViewState().copy(pictureError = null)
            )
        }
        if (getPropertyViewState().lat == null || getPropertyViewState().lng == null) {
            updatePropertyViewState(
                getPropertyViewState().copy(latLngError = "Address or Town or Postal code are not correct")
            )
        } else {
            updatePropertyViewState(
                getPropertyViewState().copy(latLngError = null)
            )
        }
        return getPropertyViewState().agentError == null &&
                getPropertyViewState().typeError == null &&
                getPropertyViewState().postalCodeError == null &&
                getPropertyViewState().addressError == null &&
                getPropertyViewState().townError == null &&
                getPropertyViewState().entryDateError == null &&
                getPropertyViewState().dateOfSaleError == null &&
                getPropertyViewState().pictureError == null &&
                getPropertyViewState().latLngError == null
    }


     private fun updatePropertyEntity(formPropertyViewState: FormPropertyViewState) =
        viewModelScope.launch {
            val listPoiSelected = mutableListOf<Int>()
            formPropertyViewState.listPoiSelectedOrNot.forEach { poi ->
                if (poi.isSelected) {
                    listPoiSelected.add(poi.poiId)
                }
            }
            val propertyId =
                propertyRepository.insertProperty(
                    PropertyEntity(
                        id = formPropertyViewState.id,
                        agentId = formPropertyViewState.agentId,
                        agentName = formPropertyViewState.agentName,
                        type = formPropertyViewState.type,
                        price = formPropertyViewState.price,
                        description = formPropertyViewState.description,
                        surface = formPropertyViewState.surface,
                        numberOfRooms = formPropertyViewState.numberOfRooms,
                        numberOfBathrooms = formPropertyViewState.numberOfBathrooms,
                        numberOfBedrooms = formPropertyViewState.numberOfBedrooms,
                        town = formPropertyViewState.town,
                        address = formPropertyViewState.address,
                        postalCode = formPropertyViewState.postalCode,
                        state = formPropertyViewState.state,
                        mainPicture = formPropertyViewState.listPicture[0].url,
                        isAvailable = formPropertyViewState.isAvailable,
                        entryDate = formPropertyViewState.entryDate,
                        dateOfSale = formPropertyViewState.dateOfSale,
                        poiSelected = listPoiSelected,
                        lat = formPropertyViewState.lat,
                        lng = formPropertyViewState.lng
                    )
                )
            formPropertyViewState.listPicture.forEach { picture ->
                val pictureToInsert = picture.copy(propertyId = propertyId)
                propertyRepository.insertPicture(pictureToInsert)
            }
        }

    fun updateLatLng(latLng: LatLng?) {
        if (latLng != null) {
            updatePropertyViewState(
                getPropertyViewState().copy(
                    lat = latLng.latitude,
                    lng = latLng.longitude
                )
            )
        } else {
            updatePropertyViewState(
                getPropertyViewState().copy(
                    lat = null,
                    lng = null
                )
            )
        }
    }

    fun updatePoi(poiId: Int, isChecked: Boolean) {
        val listPoi = mutableListOf<FormPropertyViewState.ChipPoiViewState>()
        listPoi.addAll(getPropertyViewState().listPoiSelectedOrNot)
        val index: Int = listPoi.withIndex()
            .first { poiId == it.value.poiId }
            .index
        listPoi[index] = FormPropertyViewState.ChipPoiViewState(
            poiId = poiId,
            isSelected = isChecked
        )
        updatePropertyViewState(
            getPropertyViewState().copy(
                listPoiSelectedOrNot = listPoi
            )
        )
    }

    fun updateListPicture(picture: String) {
        val listPicture = mutableListOf<PropertyPictureEntity>()
        listPicture.addAll(getPropertyViewState().listPicture)
        listPicture.add(PropertyPictureEntity(0L, 0L, picture, ""))
        updatePropertyViewState(
            getPropertyViewState().copy(
                listPicture = listPicture,
            )
        )
    }

    fun updateType(typeToUpdate: String?) {
        updatePropertyViewState(
            getPropertyViewState().copy(type = typeToUpdate?.lowercase(Locale.ROOT)
                ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() })
        )
    }

    fun updateAgent(agent: AgentEntity) {
        updatePropertyViewState(
            getPropertyViewState().copy(
                agentId = agent.agentId,
                agentName = agent.name
            )
        )
    }

    fun updateSurface(surface: String) {
        if (surface != "") {
            updatePropertyViewState(
                getPropertyViewState().copy(
                    surface = surface.toInt()
                )
            )
        } else {
            updatePropertyViewState(
                getPropertyViewState().copy(
                    surface = 0
                )
            )
        }
        surface.indices
    }

    fun updateRoom(nbrRoom: String?) {
        updatePropertyViewState(
            getPropertyViewState().copy(
                numberOfRooms = nbrRoom
            )
        )
    }

    fun updateBathRoom(nbrBathRoom: String?) {
        updatePropertyViewState(
            getPropertyViewState().copy(
                numberOfBathrooms = nbrBathRoom
            )
        )
    }

    fun updateBedRoom(nbrBedRoom: String?) {
        updatePropertyViewState(
            getPropertyViewState().copy(
                numberOfBedrooms = nbrBedRoom
            )
        )
    }

    fun updatePostalCode(postalCode: String?) {
        updatePropertyViewState(
            getPropertyViewState().copy(
                postalCode = postalCode
            )
        )
    }

    fun updateAddress(address: String?) {
        updatePropertyViewState(
            getPropertyViewState().copy(
                address = address?.lowercase(Locale.ROOT)
                    ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            )
        )
    }

    fun updateState(state: String?) {
        updatePropertyViewState(
            getPropertyViewState().copy(
                state = state
            )
        )
    }

    fun updateEntryDate(entryDate: String?) {
        updatePropertyViewState(
            getPropertyViewState().copy(
                entryDate = entryDate
            )
        )
    }

    fun updateDateOfSale(dateOfSale: String?) {
        updatePropertyViewState(
            getPropertyViewState().copy(
                dateOfSale = dateOfSale
            )
        )
    }

    fun updateDescription(description: String?) {
        updatePropertyViewState(
            getPropertyViewState().copy(
                description = description
            )
        )
    }

    fun updatePrice(price: String) {
        if (price != "") {
            updatePropertyViewState(
                getPropertyViewState().copy(
                    price = price.toInt()
                )
            )
        } else {
            updatePropertyViewState(
                getPropertyViewState().copy(
                    price = 0
                )
            )
        }
    }

    fun updateTown(town: String?) {
        updatePropertyViewState(
            getPropertyViewState().copy(
                town = town?.lowercase(Locale.ROOT)
                    ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            )
        )
    }
}
package com.openclassrooms.realestatemanager.ui.addProperty

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import com.openclassrooms.realestatemanager.databinding.FragmentAddOrUpdatePropertyBinding
import com.openclassrooms.realestatemanager.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddPropertyFragment() : Fragment() {

    private val viewModel by viewModels<AddOrUpdatePropertyViewModel>()

    lateinit var binding : FragmentAddOrUpdatePropertyBinding

    var agent: AgentEntity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddOrUpdatePropertyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val entryDatePicker =  MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select entry date")
            .build()
        binding.entryDateInput.setOnClickListener{
            entryDatePicker.show(parentFragmentManager,"Material_Date_Picker" )

            entryDatePicker.addOnPositiveButtonClickListener {
                binding.entryDateInput.setText(entryDatePicker.headerText)
                 //todo : to get the value -> https://m2.material.io/components/date-pickers/android#using-date-pickers
            }
        }
        val dateOfSalePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select sell date")
            .build()
        binding.sellingDateInput.setOnClickListener{
            dateOfSalePicker.show(parentFragmentManager, "Material_Date_Picker")

            dateOfSalePicker.addOnPositiveButtonClickListener {
                binding.sellingDateInput.setText(dateOfSalePicker.headerText)
            }

        }


        viewModel.getAllAgent().observe(viewLifecycleOwner) {
            binding.agentInput.setAdapter(ArrayAdapter(
                requireContext(),
                R.layout.list_agent_item,
                it))
        }
        binding.agentInput.setOnItemClickListener { parent, _, position, _ ->
            agent = parent.getItemAtPosition(position) as AgentEntity

        }

        binding.submitButton.setOnClickListener {
//            var propertyViewState = AddOrUpdateViewState(
//                photoList = null,
//                agent = agent,
//                type = binding.typeLayout.editText?.text.toString(),
//                district = binding.districtLayout.editText?.text.toString(),
//                price = binding.priceLayout.editText?.text.toString(),
//                description = binding.descriptionLayout.editText?.text.toString(),
//                surface = binding.surfaceLayout.editText?.text.toString().toInt(),
//                numberOfRooms = binding.nbrRoomLayout.editText?.text.toString().toInt(),
//                numberOfBathrooms = binding.nbrBathroomLayout.editText?.text.toString().toInt(),
//                numberOfBedrooms = binding.nbrBedroomLayout.editText?.text.toString().toInt(),
//                town = binding.townLayout.editText?.text.toString(),
//                address = binding.addressLayout.editText?.text.toString(),
//                postalCode = binding.postalCodeLayout.editText?.text.toString().toInt(),
//                state = binding.stateLayout.editText?.text.toString(),
//                mainPicture = null,
//                isAvailable = true,
//                entryDate = null,
//                dateOfSale = null,
//            )
//               var photoList = null


            val type = binding.typeLayout.editText?.text.toString()
            val district = binding.districtLayout.editText?.text.toString()
            val price = binding.priceLayout.editText?.text.toString()
            val description = binding.descriptionLayout.editText?.text.toString()
            val surface = binding.surfaceLayout.editText?.text.toString()
            val numberOfRooms = binding.nbrRoomLayout.editText?.text.toString()
            val numberOfBathrooms = binding.nbrBathroomLayout.editText?.text.toString()
            val numberOfBedrooms = binding.nbrBedroomLayout.editText?.text.toString()
            val town = binding.townLayout.editText?.text.toString()
            val address = binding.addressLayout.editText?.text.toString()
            val postalCode = binding.postalCodeLayout.editText?.text.toString()
            val state = binding.stateLayout.editText?.text.toString()
//               var mainPicture = null
            val isAvailable = dateOfSalePicker.selection == null
            val entryDate = entryDatePicker.selection
            val dateOfSale = dateOfSalePicker.selection
            val entryDateText = binding.entryDateLayout.editText?.text.toString()
            val dateOfSaleText = binding.sellingDateLayout.editText?.text.toString()

            if (surface == "") {
                binding.surfaceInput.requestFocus()
            } else if (numberOfRooms == "") {
                binding.nbrRoomInput.requestFocus()
            } else if (numberOfBathrooms == "") {
                binding.nbrBathroomLayout.requestFocus()
            } else if (numberOfBedrooms == "") {
                binding.nbrBedroomLayout.requestFocus()
            } else if (postalCode.length != 5) {
                binding.postalCodeInput.requestFocus()
                binding.postalCodeLayout.error = "5 numbers"
            } else if(agent == null) {
                binding.agentInput.requestFocus()
            } else if (district == "") {
                binding.districtLayout.requestFocus()
            } else if(type == "" ) {
                binding.typeLayout.requestFocus()
            }  else if (price == "" ) {
                binding.priceLayout.requestFocus()
            } else if (dateOfSaleText != "" && entryDateText == "") {
                binding.entryDateLayout.error = "you have to chose an entry date"
            } else if (dateOfSale != null && entryDate!! > dateOfSale) {
                binding.entryDateLayout.error = "must be before date of sale"
                binding.sellingDateLayout.error = "must be after entry date"
            }   else {
                val property =
                    PropertyEntity(
                        agentId = agent!!.agentId,
                        agentName = agent!!.name,
                        type = type,
                        district = district,
                        price = price.toInt(),
                        description = description,
                        surface = surface.toInt(),
                        numberOfRooms = numberOfRooms.toInt(),
                        numberOfBathrooms = numberOfBathrooms.toInt(),
                        numberOfBedrooms = numberOfBedrooms.toInt(),
                        town = town,
                        address = address,
                        postalCode = postalCode.toInt(),
                        state = state,
                        mainPicture = null,
                        isAvailable = isAvailable,
                        entryDate = entryDate,
                        dateOfSale = dateOfSale
                    )
//                 val property =
//                     PropertyEntity(
//                         agentId = agent!!.agentId,
//                         agentName = agent!!.name,
//                         type = propertyViewState.type,
//                         district = propertyViewState.district,
//                         price = propertyViewState.price?.toInt(),
//                         description = propertyViewState.description,
//                         surface = propertyViewState.surface,
//                         numberOfRooms = propertyViewState.numberOfRooms,
//                         numberOfBathrooms = propertyViewState.numberOfBathrooms,
//                         numberOfBedrooms = propertyViewState.numberOfBathrooms,
//                         town = propertyViewState.town,
//                         address = propertyViewState.address,
//                         postalCode = propertyViewState.postalCode,
//                         state = propertyViewState.state,
//                         mainPicture = propertyViewState.mainPicture,
//                         isAvailable = propertyViewState.isAvailable,
//                         entryDate = propertyViewState.entryDate,
//                         dateOfSale = propertyViewState.dateOfSale
//                     )
                viewModel.addProperty(property)
                startActivity(Intent(context, MainActivity::class.java))
            }

//        }


//            AddOrUpdatePropertyActivity().finish()
        }
    }


}
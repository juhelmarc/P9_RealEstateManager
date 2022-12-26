package com.openclassrooms.realestatemanager.ui.addProperty

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.datepicker.MaterialDatePicker
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity

import com.openclassrooms.realestatemanager.databinding.ActivityFormPropertyBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class FormPropertyActivity: AppCompatActivity() {

    private val viewModel by viewModels<FormPropertyViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFormPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Entry date picker
        val entryDatePicker =  MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select entry date")
            .build()
        binding.entryDateInput.setOnClickListener{
            entryDatePicker.show(supportFragmentManager,"Material_Date_Picker" )

            entryDatePicker.addOnPositiveButtonClickListener {
                viewModel.updateEntryDate(entryDatePicker.selection.toString())
            }
        }
        //Sell date picker
        val dateOfSalePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select sell date")
            .build()
        binding.sellingDateInput.setOnClickListener {
            dateOfSalePicker.show(supportFragmentManager, "Material_Date_Picker")

            dateOfSalePicker.addOnPositiveButtonClickListener {
                viewModel.updateDateOfSale(dateOfSalePicker.selection.toString())
            }
        }
        //AgentList
        binding.agentInput.setOnItemClickListener { parent, _, position, _ ->
            viewModel.updateAgent(parent.getItemAtPosition(position) as AgentEntity)
        }

        //Add Picture
        binding.addPictureButton.setOnClickListener {
            ImagePicker.Companion.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

         val startForProfileImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data

                if (resultCode == Activity.RESULT_OK) {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!

                    //viewModel.updatePicture(fileUri.toString())
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }


        //Update Data
        binding.typeInput.doAfterTextChanged { viewModel.updateType(it?.toString()) }
        binding.surfaceInput.doAfterTextChanged { viewModel.updateSurface(it?.toString()) }
        binding.nbrRoomInput.doAfterTextChanged { viewModel.updateRoom(it?.toString()) }
        binding.nbrBathroomInput.doAfterTextChanged { viewModel.updateBathRoom(it.toString())}
        binding.nbrBedroomInput.doAfterTextChanged { viewModel.updateBedRoom(it.toString()) }
        binding.postalCodeInput.doAfterTextChanged { viewModel.updatePostalCode(it.toString()) }
        binding.districtInput.doAfterTextChanged { viewModel.updateDistrict(it.toString()) }
        binding.addressInput.doAfterTextChanged { viewModel.updateAddress(it.toString()) }
        binding.townInput.doAfterTextChanged { viewModel.updateTown(it.toString()) }
        binding.stateInput.doAfterTextChanged { viewModel.updateState(it.toString()) }
        binding.propertyPriceInput.doAfterTextChanged { viewModel.updatePrice(it.toString()) }

        //Set data to view
        viewModel.formPropertyViewStateLiveData.observe(this) { formPropertyViewState ->
            //SetText with data
            binding.agentInput.setAdapter(
                ArrayAdapter(
                    this,
                    R.layout.list_agent_item,
                    formPropertyViewState.agentList
                )
            )
            binding.agentInput.setText(formPropertyViewState.agentName, false)
            binding.typeInput.setTextKeepState(formPropertyViewState.type)
            binding.surfaceInput.setTextKeepState(formPropertyViewState.surface.toString())
            binding.nbrRoomInput.setTextKeepState(formPropertyViewState.numberOfRooms.toString())
            binding.nbrBathroomInput.setTextKeepState(formPropertyViewState.numberOfBathrooms.toString())
            binding.nbrBedroomInput.setTextKeepState(formPropertyViewState.numberOfBedrooms.toString())
            binding.postalCodeInput.setTextKeepState(formPropertyViewState.postalCode.toString())
            binding.districtInput.setTextKeepState(formPropertyViewState.district)
            binding.addressInput.setTextKeepState(formPropertyViewState.address)
            binding.townInput.setTextKeepState(formPropertyViewState.town)
            binding.stateInput.setTextKeepState(formPropertyViewState.state)
            if(formPropertyViewState.entryDate != "") {
                binding.entryDateInput.setText(formatDate(formPropertyViewState.entryDate?.toLong()))
            }
            if(formPropertyViewState.dateOfSale != "") {
                binding.sellingDateInput.setText(formatDate(formPropertyViewState.dateOfSale?.toLong()))
            }
            binding.propertyPriceInput.setTextKeepState(formPropertyViewState.price.toString())

            //Set Errors
            binding.agentLayout.error = formPropertyViewState.agentError
            binding.typeLayout.error = formPropertyViewState.typeError
            binding.postalCodeLayout.error = formPropertyViewState.postalCodeError
            binding.districtLayout.error = formPropertyViewState.districtError
            binding.addressLayout.error = formPropertyViewState.addressError
            binding.townLayout.error = formPropertyViewState.townError
            binding.entryDateLayout.error = formPropertyViewState.entryDateError
            binding.sellingDateLayout.error = formPropertyViewState.dateOfSaleError
        }

        binding.submitButton.setOnClickListener {
                viewModel.onSubmitButtonClicked().observe(this) { formHasNoError ->
                    if(formHasNoError) {
                        finish()
                    }
                }
        }
    }

    private fun formatDate(dateMilli: Long?): String {
        val format: String = "MMM dd.yyyy"
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat(format, Locale.US)
        return simpleDateFormat.format(dateMilli)
    }


}
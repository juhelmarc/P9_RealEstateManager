package com.openclassrooms.realestatemanager.ui.formProperty

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.datepicker.MaterialDatePicker
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity
import com.openclassrooms.realestatemanager.databinding.ActivityFormPropertyBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class FormPropertyActivity : AppCompatActivity() {

    private val viewModel by viewModels<FormPropertyViewModel>()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFormPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(savedInstanceState == null) {
            viewModel.getInitialViewStateLiveData().observe(this) {
                viewModel.setInitialViewState(it)
            }
        }

        //Entry date picker
        val entryDatePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select entry date")
            .build()
        binding.entryDateInput.setOnClickListener {
            entryDatePicker.show(supportFragmentManager, "Material_Date_Picker")
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
        //RecyclerView
        val recyclerView: RecyclerView = binding.formRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val adapter = FormPropertyAdapter()
        recyclerView.adapter = adapter
        //Poi
        val recyclerViewChip: RecyclerView = binding.poiList
        recyclerViewChip.layoutManager = FlexboxLayoutManager(this)
        val chipAdapter = ChipAdapter { poiId, isChecked -> viewModel.updatePoi(poiId, isChecked) }
        recyclerViewChip.adapter = chipAdapter
        //Add Picture
        val startForProfileImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.data?.toString().let {
                            if (it != null) {
                                viewModel.updateListPicture(it)
                            }
                        }
                    }
                    ImagePicker.RESULT_ERROR -> {
                        Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        binding.addPictureButton.setOnClickListener {
            ImagePicker.Companion.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        binding.typeInput.doAfterTextChanged { viewModel.updateType(binding.typeInput.text.toString()) }

        binding.surfaceInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.updateSurface(binding.surfaceInput.text.toString())
            }
        }

        binding.nbrRoomInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.updateRoom(binding.nbrRoomInput.text.toString())
            }
        }

        binding.nbrBathroomInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.updateBathRoom(binding.nbrBathroomInput.text.toString())
            }
        }

        binding.nbrBedroomInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.updateBedRoom(binding.nbrBedroomInput.text.toString())
            }
        }
        binding.postalCodeInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.updatePostalCode(binding.postalCodeInput.text.toString())
                if (binding.townInput.text.toString() != "" && binding.addressInput.text.toString() != "" && binding.postalCodeInput.text.toString() != "") {
                    getLatLngFromAddress(binding.townInput.text.toString() + ", " + binding.addressInput.text.toString() + ", " + binding.postalCodeInput.toString()) {
                        viewModel.updateLatLng(it)
                    }
                }
            }
        }
        binding.addressInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.updateAddress(binding.addressInput.text.toString())
                if (binding.townInput.text.toString() != "" && binding.addressInput.text.toString() != "" && binding.postalCodeInput.text.toString() != "") {
                    getLatLngFromAddress(binding.townInput.text.toString() + ", " + binding.addressInput.text.toString() + ", " + binding.postalCodeInput.toString()) {
                        viewModel.updateLatLng(it)
                    }
                }
            }
        }
        binding.townInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.updateTown(binding.townInput.text.toString())
                if (binding.townInput.text.toString() != "" && binding.addressInput.text.toString() != "" && binding.postalCodeInput.text.toString() != "") {
                    getLatLngFromAddress(binding.townInput.text.toString() + ", " + binding.addressInput.text.toString() + ", " + binding.postalCodeInput.toString()) {
                        viewModel.updateLatLng(it)
                    }

                }
            }
        }
        binding.stateInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.updateState(binding.stateInput.text.toString())
            }
        }
        binding.descriptionInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.updateDescription(binding.descriptionInput.text.toString())
            }
        }
        binding.propertyPriceInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.updatePrice(binding.propertyPriceInput.text.toString())
            }
        }
        binding.propertyPriceInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.updatePrice(binding.propertyPriceInput.text.toString())
            }
        }
        //AgentList
        binding.agentInput.setOnItemClickListener { parent, _, position, _ ->
            viewModel.updateAgent(parent.getItemAtPosition(position) as AgentEntity)
        }
        viewModel.getAgentListLiveData().observe(this) { agentList ->
            binding.agentInput.setAdapter(
                ArrayAdapter(
                    this,
                    R.layout.list_agent_item,
                    agentList
                )
            )
        }
        //Set data to view
        viewModel.getViewStateLiveData().observe(this) { formPropertyViewState ->

            val surface = formPropertyViewState.surface ?: 0
            val price = formPropertyViewState.price ?: 0
            //SetText with data
            binding.agentInput.setText(formPropertyViewState.agentName, false)
            binding.typeInput.setTextKeepState(formPropertyViewState.type)
            binding.surfaceInput.setTextKeepState(surface.toString())
            binding.nbrRoomInput.setTextKeepState(formPropertyViewState.numberOfRooms.toString())
            binding.nbrBathroomInput.setTextKeepState(formPropertyViewState.numberOfBathrooms.toString())
            binding.nbrBedroomInput.setTextKeepState(formPropertyViewState.numberOfBedrooms.toString())
            binding.postalCodeInput.setTextKeepState(formPropertyViewState.postalCode.toString())
            binding.descriptionInput.setTextKeepState(formPropertyViewState.description.toString())
            binding.addressInput.setTextKeepState(formPropertyViewState.address)
            binding.townInput.setTextKeepState(formPropertyViewState.town)
            binding.stateInput.setTextKeepState(formPropertyViewState.state)

            if (formPropertyViewState.entryDate != "" && formPropertyViewState.entryDate != null) {
                binding.entryDateInput.setText(formatDate(formPropertyViewState.entryDate.toLong()))
            } else {
                binding.entryDateInput.setText("")
            }
            if (formPropertyViewState.dateOfSale != "" && formPropertyViewState.dateOfSale != null) {
                binding.sellingDateInput.setText(formatDate(formPropertyViewState.dateOfSale.toLong()))
            } else {
                binding.sellingDateInput.setText("")
            }
            binding.propertyPriceInput.setTextKeepState(price.toString())
            //adapters
            adapter.submitList(formPropertyViewState.listPicture)
            chipAdapter.submitList(formPropertyViewState.listPoiSelectedOrNot)

            //Set Errors
            binding.agentLayout.error = formPropertyViewState.agentError
            binding.typeLayout.error = formPropertyViewState.typeError
            binding.postalCodeLayout.error = formPropertyViewState.postalCodeError
            binding.addressLayout.error = formPropertyViewState.addressError
            binding.townLayout.error = formPropertyViewState.townError
            binding.entryDateLayout.error = formPropertyViewState.entryDateError
            binding.sellingDateLayout.error = formPropertyViewState.dateOfSaleError
            binding.errorAddress.text = formPropertyViewState.latLngError
            binding.errorPicture.text = formPropertyViewState.pictureError
        }

        binding.submitButton.setOnClickListener {
            binding.poiList.requestFocus()
            if (viewModel.onSubmitButtonClicked()) {
                if (!viewModel.getIsAnUpdate()) {
                    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_baseline_add_home_work_24)
                        .setContentTitle("Property :")
                        .setContentText("Added successfully")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)

                    with(NotificationManagerCompat.from(this)) {
                        notify(NOTIFICATION_ID, builder.build())
                    }
                }
                finish()
            } else {
                binding.formRecyclerview.clearFocus()
                binding.poiList.clearFocus()
                binding.addressInput.clearFocus()
                binding.postalCodeInput.clearFocus()
                binding.townInput.clearFocus()
                binding.agentInput.clearFocus()
                viewModel.getViewStateLiveData().observe(this) { viewState ->
                    val error: String =
                        viewState.pictureError
                            ?: (viewState.addressError
                                ?: (viewState.postalCodeError
                                    ?: (viewState.townError
                                        ?: (viewState.agentError
                                            ?: (viewState.typeError
                                                ?: (viewState.latLngError ?: ""))))))
                    when (error) {
                        viewState.pictureError ->
                            binding.formRecyclerview.requestFocus()
                        viewState.addressError ->
                            binding.addressInput.requestFocus()
                        viewState.postalCodeError ->
                            binding.postalCodeInput.requestFocus()
                        viewState.townError ->
                            binding.townInput.requestFocus()
                        viewState.typeError ->
                            binding.typeInput.requestFocus()
                        viewState.agentError ->
                            binding.agentInput.requestFocus()
                        viewState.latLngError ->
                            binding.formRecyclerview.requestFocus()
                    }
                }
            }
        }
    }

    private fun formatDate(dateMilli: Long): String {
        val format = "MMM dd.yyyy"
        val simpleDateFormat = SimpleDateFormat(format, Locale.US)
        return simpleDateFormat.format(dateMilli)
    }

    private fun getLatLngFromAddress(address: String, onSuccess: (latLng :LatLng?) -> Unit) {
        val geocoder = Geocoder(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocationName(address, 5, object:Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<Address>) {
                    onSuccess(getLatLngFromAddress(addresses))
                }
                override fun onError(errorMessage: String?) {
                    super.onError(errorMessage)
                    onSuccess(getLatLngFromAddress(emptyList()))
                }
            })
        } else {
            geocoder.getFromLocationName(address, 5)?.let {
                onSuccess(getLatLngFromAddress(it))
            }?: kotlin.run {
                onSuccess(getLatLngFromAddress(emptyList()))
            }
        }
    }

    private fun getLatLngFromAddress(listAddress : List<Address>) : LatLng? {
       return if (listAddress.isNotEmpty()) {
           LatLng((listAddress as MutableList<Address>)[0].latitude, listAddress[0].longitude)
        } else {
            null
        }
    }

    companion object {
        private const val CHANNEL_ID = "ChannelId"
        private const val NOTIFICATION_ID = 12
    }
}

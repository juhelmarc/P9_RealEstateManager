package com.openclassrooms.realestatemanager.ui.filter

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.indexOf
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.RangeSlider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.dao.PropertyPriceSurface
import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity

import com.openclassrooms.realestatemanager.databinding.ActivityFilterBinding
import com.openclassrooms.realestatemanager.ui.main.MainViewModel

import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class FilterActivity : AppCompatActivity() {

    private val viewModel by viewModels<FilterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rangeSliderPrice = binding.filterPrice
        val rangeSliderSurface = binding.filterSurface

        viewModel.filterViewStateLiveData.observe(this) { viewState ->
            viewModel.setInitialQueryFilter(viewState)
            if(viewState.minPrice!! != viewState.maxPrice!!) {
                rangeSliderPrice.isEnabled = true
                rangeSliderPrice.setBackgroundColor(getColor(R.color.white))
                rangeSliderPrice.valueFrom = viewState.minPrice.toFloat()
                rangeSliderPrice.valueTo = viewState.maxPrice.toFloat()
                rangeSliderPrice.values =
                    listOf(viewState.minPrice.toFloat(), viewState.maxPrice.toFloat())
            }  else {
                rangeSliderPrice.isEnabled = false
                rangeSliderPrice.setBackgroundColor(getColor(R.color.grey_font))
            }

            if(viewState.minSurface!! != viewState.maxSurface!!) {
                rangeSliderSurface.isEnabled = true
                rangeSliderSurface.setBackgroundColor(getColor(R.color.white))
                rangeSliderSurface.valueFrom = viewState.minSurface.toFloat()
                rangeSliderSurface.valueTo = viewState.maxSurface.toFloat()
                rangeSliderSurface.values = listOf(viewState.minSurface.toFloat() , viewState.maxSurface.toFloat())
            }  else {
                rangeSliderSurface.isEnabled = false
                rangeSliderSurface.setBackgroundColor(getColor(R.color.grey_font))
            }

            viewModel.queryFilterLiveData.observe(this) { queryFilterViewState ->
                val query: String = queryFilterViewState.toString()
                viewModel.getNbrOfPropertyWithThisQuery(query).observe(this) { listPropertyEntity ->
                    val numberOfPropertyFound = "Nbr of property found : ${listPropertyEntity.size}"
                    binding.numberOfPropertyFound.text = numberOfPropertyFound
                    if(listPropertyEntity.isEmpty()) {
                        binding.numberOfPropertyFound.setTextColor(resources.getColor(R.color.red))
                    } else {
                        binding.numberOfPropertyFound.setTextColor(resources.getColor(R.color.black))
                    }
                    binding.filterButton.setOnClickListener {
                        if(listPropertyEntity.isEmpty()) {
                            Toast.makeText(this, "No property found, change filter", Toast.LENGTH_SHORT).show()
                        } else {
                            setResult(RESULT_OK, Intent().putExtra("data",query))
                            finish()
                        }
                    }
                }
            }
            //AgentList
            binding.agentInput.setOnItemClickListener { parent, _, position, _ ->
                viewModel.updateQueryFilterAgent(parent.getItemAtPosition(position).toString())
            }
            val listAgent = mutableListOf<AgentEntity>()
            listAgent.addAll(viewState.listAgent)
            listAgent.add(AgentEntity(5L, ""))
            binding.agentInput.setAdapter(
                ArrayAdapter(
                    this,
                    R.layout.list_agent_item,
                    listAgent
                )
            )
            //Mutil drop down memnu TYPE
            val selectedType: BooleanArray = BooleanArray(viewState.listOfType!!.size)
            val listTypeSelected: MutableList<String> = mutableListOf()
            val listArrayType = viewState.listOfType.toTypedArray()
            binding.textViewType.setOnClickListener{
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Select type")
                builder.setCancelable(false)
                builder.setMultiChoiceItems(listArrayType, selectedType, DialogInterface.OnMultiChoiceClickListener { _, which, isChecked ->
                    if(isChecked) {
                        listTypeSelected.add(listArrayType[which])
                    } else {
                        listTypeSelected.remove(listArrayType[which])
                    }
                } )
                builder.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                    val typeStringBuilder = StringBuilder("")
                    listTypeSelected.forEach { 
                        typeStringBuilder.append(it)
                        if(listTypeSelected.indexOf(it) != listTypeSelected.size - 1) {
                            typeStringBuilder.append(", ")
                        }
                    }
                    binding.textViewType.text = typeStringBuilder
                    viewModel.updateQueryFilterListType(listTypeSelected)
                })
                builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                })
                builder.setNeutralButton("Clear all", DialogInterface.OnClickListener { _, _ ->
                    selectedType.forEach {
                        selectedType[selectedType.indexOf(it)] = false
                        listTypeSelected.clear()

                    }
                    binding.textViewType.text = ""
                    viewModel.updateQueryFilterListType(listOf())
                })
                builder.show()
            }
            //Mutil drop down memnu TOWN
            val selectedTown: BooleanArray = BooleanArray(viewState.listOfTown!!.size)
            val listTownSelected: MutableList<String> = mutableListOf()
            val listArrayTown = viewState.listOfTown.toTypedArray()
            binding.textViewTown.setOnClickListener{
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Select town")
                builder.setCancelable(false)
                builder.setMultiChoiceItems(listArrayTown, selectedTown, DialogInterface.OnMultiChoiceClickListener { _, which, isChecked ->
                    if(isChecked) {
                        listTownSelected.add(listArrayTown[which])
                    } else {
                        listTownSelected.remove(listArrayTown[which])
                    }
                } )
                builder.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                    val typeStringBuilder = StringBuilder("")
                    listTownSelected.forEach {
                        typeStringBuilder.append(it)
                        if(listTownSelected.indexOf(it) != listTownSelected.size - 1) {
                            typeStringBuilder.append(", ")
                        }
                    }
                    binding.textViewTown.text = typeStringBuilder
                    viewModel.updateQueryFilterListTown(listTownSelected)
                })
                builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                })
                builder.setNeutralButton("Clear all", DialogInterface.OnClickListener { _, _ ->
                    selectedTown.forEach {
                        selectedTown[selectedTown.indexOf(it)] = false
                        listTownSelected.clear()

                    }
                    binding.textViewTown.text = ""
                    viewModel.updateQueryFilterListTown(listOf())
                })
                builder.show()
            }
        }
        rangeSliderPrice.setLabelFormatter { value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("USD")
            format.format(value.toInt())
        }

        rangeSliderSurface.setLabelFormatter { value: Float ->
            val format = NumberFormat.getInstance()
            format.maximumFractionDigits = 0
            format.format(value.toInt())
        }

        rangeSliderPrice.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being stopped
            }
        })
        rangeSliderPrice.addOnChangeListener { rangeSlider, _, _ ->
            // Responds to when slider's value is changed
            viewModel.updateQueryFilterPrice(rangeSlider.values[0].toInt(), rangeSlider.values[1].toInt() )
        }

        rangeSliderSurface.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being stopped
            }
        })
        rangeSliderSurface.addOnChangeListener { rangeSlider, _, _ ->
            // Responds to when slider's value is changed
            viewModel.updateQueryFilterSurface(rangeSlider.values[0].toInt(), rangeSlider.values[1].toInt())
        }
    }
}
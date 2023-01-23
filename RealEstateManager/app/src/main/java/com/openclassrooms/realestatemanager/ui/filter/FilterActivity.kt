package com.openclassrooms.realestatemanager.ui.filter

import android.content.DialogInterface
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.slider.RangeSlider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.models.CurrentFilterValue
import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity

import com.openclassrooms.realestatemanager.databinding.ActivityFilterBinding

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

        viewModel.filterFeatureViewStateLiveData.observe(this) { filterFeatureViewState ->
            viewModel.registerCurrentFilterValueAndQuery(filterFeatureViewState)
            if(filterFeatureViewState.minPrice != filterFeatureViewState.maxPrice) {
                rangeSliderPrice.isEnabled = true
                rangeSliderPrice.setBackgroundColor(getColor(R.color.white))
                rangeSliderPrice.valueFrom = filterFeatureViewState.minPrice.toFloat()
                rangeSliderPrice.valueTo = filterFeatureViewState.maxPrice.toFloat()
                if(filterFeatureViewState.minPriceSelected != null && filterFeatureViewState.maxPriceSelected != null) {
                    rangeSliderPrice.values =
                        listOf(filterFeatureViewState.minPriceSelected.toFloat(), filterFeatureViewState.maxPriceSelected.toFloat())
                } else {
                    rangeSliderPrice.values =
                        listOf(filterFeatureViewState.minPrice.toFloat(), filterFeatureViewState.maxPrice.toFloat())
                }
            }  else {
                rangeSliderPrice.isEnabled = false
                rangeSliderPrice.setBackgroundColor(getColor(R.color.grey_font))
            }
            if(filterFeatureViewState.minSurface != filterFeatureViewState.maxSurface) {
                rangeSliderSurface.isEnabled = true
                rangeSliderSurface.setBackgroundColor(getColor(R.color.white))
                rangeSliderSurface.valueFrom = filterFeatureViewState.minSurface.toFloat()
                rangeSliderSurface.valueTo = filterFeatureViewState.maxSurface.toFloat()
                if(filterFeatureViewState.minSurfaceSelected != null && filterFeatureViewState.maxSurfaceSelected != null) {
                    rangeSliderSurface.values = listOf(filterFeatureViewState.minSurfaceSelected.toFloat() , filterFeatureViewState.maxSurfaceSelected.toFloat())
                } else {
                    rangeSliderSurface.values = listOf(filterFeatureViewState.minSurface.toFloat() , filterFeatureViewState.maxSurface.toFloat())
                }

            }  else {
                rangeSliderSurface.isEnabled = false
                rangeSliderSurface.setBackgroundColor(getColor(R.color.grey_font))
            }

            val query: String = viewModel.toQuery(filterFeatureViewState)
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
                        viewModel.registerCurrentFilterValueAndQuery(filterFeatureViewState)
                        viewModel.registerFilterQueryWhenSubmitButtonClicked(query)
                        finish()
                    }
                }
            }
 //           }
            binding.clearFilterButton.setOnClickListener {
                viewModel.deleteCurrentFilter()
            }
            //AgentList
            binding.agentInput.setOnItemClickListener { parent, _, position, _ ->
                viewModel.updateQueryFilterAgent(parent.getItemAtPosition(position).toString())

            }

            val listAgent = mutableListOf<AgentEntity>()
            listAgent.addAll(filterFeatureViewState.listAgent)
            listAgent.add(AgentEntity(5L, ""))
            val arrayAdapter = ArrayAdapter(
                this,
                R.layout.list_agent_item,
                listAgent
            )
            val agentSelected: AgentEntity? = listAgent.find { it.name == filterFeatureViewState.agentNameSelected }
            val itemPosition: Int = arrayAdapter.getPosition(agentSelected)
            binding.agentInput.setText(arrayAdapter.getItem(itemPosition).toString())
            binding.agentInput.setAdapter(arrayAdapter)
            binding.agentInput.dismissDropDown()

            //Mutil drop down memnu TYPE

            val listTypeSelected: MutableList<String> = mutableListOf()
            val listArrayType = filterFeatureViewState.listOfType.toTypedArray()
            val selectedType: BooleanArray = BooleanArray(listArrayType.size)
            if (filterFeatureViewState.listOfTypeSelected.isNotEmpty()) {
                filterFeatureViewState.listOfType.forEach { type ->
                    if(filterFeatureViewState.listOfTypeSelected.contains(type)) {
                        listTypeSelected.add(listArrayType[filterFeatureViewState.listOfType.indexOf(type)])
                        selectedType[filterFeatureViewState.listOfType.indexOf(type)] = true
                    } else {
                        listTypeSelected.remove(listArrayType[filterFeatureViewState.listOfType.indexOf(type)])
                        selectedType[filterFeatureViewState.listOfType.indexOf(type)] = false
                    }
                }
            }
            val typeStringBuilder = StringBuilder("")
            listTypeSelected.forEach {
                typeStringBuilder.append(it)
                if(listTypeSelected.indexOf(it) != listTypeSelected.size - 1) {
                    typeStringBuilder.append(", ")
                }
            }
            binding.textViewType.text = typeStringBuilder

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
                    viewModel.updateQueryFilterListType(listTypeSelected)
                })
                builder.show()
            }
            //Mutil drop down memnu TOWN
            val selectedTown: BooleanArray = BooleanArray(filterFeatureViewState.listOfTown.size)
            val listTownSelected: MutableList<String> = mutableListOf()
            val listArrayTown = filterFeatureViewState.listOfTown.toTypedArray()
            if (filterFeatureViewState.listOfTownSelected.isNotEmpty()) {
                filterFeatureViewState.listOfTown.forEach { town ->
                    if(filterFeatureViewState.listOfTownSelected.contains(town)) {
                        listTownSelected.add(listArrayTown[filterFeatureViewState.listOfTown.indexOf(town)])
                        selectedTown[filterFeatureViewState.listOfTown.indexOf(town)] = true
                    } else {
                        listTownSelected.remove(listArrayTown[filterFeatureViewState.listOfTown.indexOf(town)])
                        selectedTown[filterFeatureViewState.listOfTown.indexOf(town)] = false
                    }
                }
            }
            val townStringBuilder = StringBuilder("")
            listTownSelected.forEach {
                townStringBuilder.append(it)
                if(listTownSelected.indexOf(it) != listTownSelected.size - 1) {
                    townStringBuilder.append(", ")
                }
            }
            binding.textViewTown.text = townStringBuilder
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
                    val townStringBuilder = StringBuilder("")
                    listTownSelected.forEach {
                        typeStringBuilder.append(it)
                        if(listTownSelected.indexOf(it) != listTownSelected.size - 1) {
                            townStringBuilder.append(", ")
                        }
                    }
                    binding.textViewTown.text = townStringBuilder
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
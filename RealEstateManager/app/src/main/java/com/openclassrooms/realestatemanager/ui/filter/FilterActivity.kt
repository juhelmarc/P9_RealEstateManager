package com.openclassrooms.realestatemanager.ui.filter

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.RangeSlider
import com.openclassrooms.realestatemanager.R

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
            if(viewState.minPrice != null && viewState.minPrice != 0 && viewState.maxPrice != null && viewState.maxPrice != 0 && viewState.minPrice < viewState.maxPrice) {
                rangeSliderPrice.isEnabled = true
                rangeSliderPrice.setBackgroundColor(getColor(R.color.white))
                rangeSliderPrice.valueFrom = viewState.minPrice.toFloat()
                rangeSliderPrice.valueTo =  viewState.maxPrice.toFloat()
                rangeSliderPrice.values = listOf(viewState.minPrice.toFloat() , viewState.maxPrice.toFloat())
//                rangeSliderPrice.valueTo = viewState.maxPrice.toFloat()
            } else if (viewState.minPrice == viewState.maxPrice && viewState.minPrice != null){
                rangeSliderPrice.isEnabled = false
                rangeSliderPrice.setBackgroundColor(getColor(R.color.grey_font))
                rangeSliderPrice.values = listOf(viewState.minPrice.toFloat())
//                rangeSliderPrice.valueFrom = viewState.minPrice.toFloat()
//                rangeSliderPrice.valueTo = viewState.minPrice.toFloat() + 1L

            } else {
                rangeSliderPrice.isEnabled = false
                rangeSliderPrice.setBackgroundColor(getColor(R.color.grey_font))
                rangeSliderPrice.values = listOf(0F)
//                rangeSliderPrice.valueFrom = 0F
//                rangeSliderPrice.valueTo = 2000000F
            }
            if(viewState.minSurface != null && viewState.minSurface != 0 && viewState.maxSurface != null && viewState.maxSurface != 0 && viewState.minSurface < viewState.maxSurface) {
                rangeSliderSurface.isEnabled = true
                rangeSliderSurface.setBackgroundColor(getColor(R.color.white))
                rangeSliderSurface.valueFrom = viewState.minSurface.toFloat()
                rangeSliderSurface.valueTo = viewState.maxSurface.toFloat()
                rangeSliderSurface.values = listOf(viewState.minSurface.toFloat() , viewState.maxSurface.toFloat())
//                rangeSliderPrice.valueTo = viewState.maxPrice.toFloat()
            } else if (viewState.minSurface == viewState.maxSurface && viewState.minSurface != null){
                rangeSliderSurface.isEnabled = false
                rangeSliderSurface.setBackgroundColor(getColor(R.color.grey_font))
                rangeSliderSurface.values = listOf(viewState.minSurface.toFloat())
                rangeSliderSurface.valueFrom = viewState.minSurface.toFloat()
                rangeSliderSurface.valueTo = viewState.minSurface.toFloat()
//                rangeSliderPrice.valueFrom = viewState.minPrice.toFloat()
//                rangeSliderPrice.valueTo = viewState.minPrice.toFloat() + 1L

            } else {
                rangeSliderSurface.isEnabled = false
                rangeSliderSurface.setBackgroundColor(getColor(R.color.grey_font))
                rangeSliderSurface.values = listOf(0F)
                rangeSliderSurface.valueFrom = viewState.minSurface!!.toFloat()
                rangeSliderSurface.valueTo = viewState.minSurface.toFloat()
//                rangeSliderPrice.valueFrom = 0F
//                rangeSliderPrice.valueTo = 2000000F
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

//        rangeSliderPrice.addOnChangeListener { rangeSlider, value, fromUser ->
//            // Responds to when slider's value is changed
//        }

        rangeSliderSurface.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being stopped
            }
        })

//        rangeSliderSurface.addOnChangeListener { rangeSlider, value, fromUser ->
//            // Responds to when slider's value is changed
//        }


        val data = "SELECT * FROM PropertyEntity WHERE price = 250000"

        binding.filterButton.setOnClickListener {
            setResult(RESULT_OK, Intent().putExtra("data",data))
            finish()
        }
    }





}
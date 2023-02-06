package com.openclassrooms.realestatemanager.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.flexbox.FlexboxLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentDetailBinding
import com.openclassrooms.realestatemanager.ui.detailslider.DetailSliderActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailViewModel>()
    private lateinit var adapter: DetailAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //POIs
        val recyclerViewChip: RecyclerView = binding.poiListDetail
        recyclerViewChip.layoutManager = FlexboxLayoutManager(requireContext())
        val chipAdapter = DetailChipAdapter()
        recyclerViewChip.adapter = chipAdapter
        //Pictures
        val recyclerView: RecyclerView = binding.detailRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        adapter = DetailAdapter()
        recyclerView.adapter = adapter

        adapter.onItemClicked = {
            startActivity(Intent(context, DetailSliderActivity::class.java))
        }

        viewModel.detailLiveData.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is DetailViewState.Selected -> {
                    binding.descriptionText.text = viewState.description
                    binding.surfaceValue.text = viewState.surface.toString()
                    binding.nbrRoomValue.text = viewState.numberOfRooms
                    binding.nbrBathroomValue.text = viewState.numberOfBathrooms
                    binding.nbrBedroomValue.text = viewState.numberOfBedRooms
                    binding.addressValue.text = viewState.address
                    binding.townValue.text = viewState.town
                    binding.postalCodeValue.text = viewState.postalCode
                    binding.stateValue.text = viewState.state
                    binding.staticMap.load(
                        "https://maps.googleapis.com/maps/api/staticmap?" +
                                "&markers=" + viewState.town + ", " + viewState.address + ", " + viewState.postalCode +
                                "&zoom=19" +
                                "&size=400x400" +
                                "&maptype=roadmap" +
                                "&key=AIzaSyCyn3_Hvu0b4PlANLre07Wvme5VCR4qewo"
                    )
                    binding.staticMap.setBackgroundColor(resources.getColor(R.color.black))
                    binding.emptyListProperty.visibility = View.INVISIBLE
                    if (viewState.entryDate != null && viewState.entryDate != "") {
                        binding.entryDateValue.text =
                            "Entry date : ${formatDate(viewState.entryDate.toLong())}"
                    } else {
                        binding.entryDateValue.text = ""
                    }
                    if (viewState.sellingDate != null && viewState.sellingDate != "") {
                        binding.sellingDateValue.text =
                            "Entry date : ${formatDate(viewState.sellingDate.toLong())}"
                    } else {
                        binding.sellingDateValue.text = ""
                    }
                    adapter.submitList(viewState.listPicture)
                    chipAdapter.submitList(viewState.poiSelected)
                }
                is DetailViewState.Empty -> {
                    binding.emptyListProperty.visibility = View.VISIBLE
                    binding.emptyListProperty.text = "Add your first property"
                }
            }
        }
    }

    private fun formatDate(dateMilli: Long): String {
        val format: String = "MMM dd.yyyy"
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat(format, Locale.US)
        return simpleDateFormat.format(dateMilli)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
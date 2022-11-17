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
import coil.imageLoader
import coil.load
import com.google.android.gms.maps.MapFragment
import com.openclassrooms.realestatemanager.databinding.BottomViewDetailBinding
import com.openclassrooms.realestatemanager.databinding.FragmentDetailBinding
import com.openclassrooms.realestatemanager.ui.detailslider.DetailSliderActivity

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment: Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!


    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val recyclerView : RecyclerView = binding.detailRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false )
        val adapter = DetailAdapter()
        recyclerView.adapter = adapter
        viewModel.listPictureLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        adapter.onItemClicked1 = {
            startActivity(Intent(context, DetailSliderActivity :: class.java))
            viewModel.setPictureClicked(it)
        }
        viewModel.detailLiveData.observe(viewLifecycleOwner) {
            binding.staticMap.load("https://maps.googleapis.com/maps/api/staticmap?" +
                    "&markers=" + it.town + ", " + it.address + ", " + it.postalCode +
                    "&zoom=18" +
                    "&size=400x400" +
                    "&key=AIzaSyCyn3_Hvu0b4PlANLre07Wvme5VCR4qewo" )
        }
        bindView(binding)


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindView(binding: FragmentDetailBinding) {
        viewModel.detailLiveData.observe(viewLifecycleOwner) {
            binding.descriptionText.text = it.description
            binding.surfaceValue.text = it.surface.toString()
            binding.nbrRoomValue.text = it.numberOfRooms.toString()
            binding.nbrBathroomValue.text = it.numberOfBathrooms.toString()
            binding.nbrBedroomValue.text = it.numberOfBedRooms.toString()
            binding.addressValue.text = it.address
            binding.townValue.text = it.town
            binding.postalCodeValue.text = it.postalCode
            binding.stateValue.text = it.state
        }
    }

}
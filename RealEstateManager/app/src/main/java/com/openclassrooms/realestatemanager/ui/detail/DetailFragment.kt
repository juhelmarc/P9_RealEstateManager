package com.openclassrooms.realestatemanager.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.load
import com.google.android.gms.maps.MapFragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.BottomViewDetailBinding
import com.openclassrooms.realestatemanager.databinding.FragmentDetailBinding
import com.openclassrooms.realestatemanager.ui.detailslider.DetailSliderActivity

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment: Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailViewModel>()
    private lateinit var adapter: DetailAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
//        defaultBinding()
//        configureListPictureAdapterDefault()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureListPictureAdapter()
        bindView()

        val recyclerView : RecyclerView = binding.detailRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false )
        adapter = DetailAdapter()
        recyclerView.adapter = adapter

        adapter.onItemClicked = {
            startActivity(Intent(context, DetailSliderActivity :: class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configureListPictureAdapter() {
        viewModel.listPictureLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
    //TODO créer un DetailViewState.NotSelected -> pour afficher le detail de l'id = 1
    private fun bindView() {
        viewModel.detailLiveData.observe(viewLifecycleOwner) { property ->
            when(property) {
                is DetailViewState.Selected -> {
                    binding.descriptionText.text = property.description
                    binding.surfaceValue.text = property.surface.toString()
                    binding.nbrRoomValue.text = property.numberOfRooms.toString()
                    binding.nbrBathroomValue.text = property.numberOfBathrooms.toString()
                    binding.nbrBedroomValue.text = property.numberOfBedRooms.toString()
                    binding.addressValue.text = property.address
                    binding.townValue.text = property.town
                    binding.postalCodeValue.text = property.postalCode.toString()
                    binding.stateValue.text = property.state
                    binding.staticMap.load(
                        "https://maps.googleapis.com/maps/api/staticmap?" +
                                "&markers=" + property.town + ", " + property.address + ", " + property.postalCode.toString() +
                                "&zoom=19" +
                                "&size=400x400" +
                                "&maptype=roadmap" +
                                "&key=AIzaSyCyn3_Hvu0b4PlANLre07Wvme5VCR4qewo"
                    )
                    binding.staticMap.setBackgroundColor(resources.getColor(R.color.black))
                    binding.emptyListProperty.visibility = View.INVISIBLE
                }
                is DetailViewState.Empty -> {
                    binding.emptyListProperty.visibility = View.VISIBLE

                }
            }

        }
    }
    private fun configureListPictureAdapterDefault() {
        viewModel.listPictureLiveDataDefault.observe(viewLifecycleOwner) { defaultListPicture ->
            adapter.submitList(defaultListPicture)
        }
    }

    private fun defaultBinding() {
        viewModel.detailLiveDataDefault.observe(viewLifecycleOwner) { defaultDetailViewState ->
            when(defaultDetailViewState) {
            is DetailViewState.Selected -> {
                binding.descriptionText.text = defaultDetailViewState.description
                binding.surfaceValue.text = defaultDetailViewState.surface.toString()
                binding.nbrRoomValue.text = defaultDetailViewState.numberOfRooms.toString()
                binding.nbrBathroomValue.text = defaultDetailViewState.numberOfBathrooms.toString()
                binding.nbrBedroomValue.text = defaultDetailViewState.numberOfBedRooms.toString()
                binding.addressValue.text = defaultDetailViewState.address
                binding.townValue.text = defaultDetailViewState.town
                binding.postalCodeValue.text = defaultDetailViewState.postalCode.toString()
                binding.stateValue.text = defaultDetailViewState.state
                binding.staticMap.load(
                    "https://maps.googleapis.com/maps/api/staticmap?" +
                            "&markers=" + defaultDetailViewState.town + ", " + defaultDetailViewState.address + ", " + defaultDetailViewState.postalCode.toString() +
                            "&zoom=19" +
                            "&size=400x400" +
                            "&maptype=roadmap" +
                            "&key=AIzaSyCyn3_Hvu0b4PlANLre07Wvme5VCR4qewo"
                )
                binding.staticMap.setBackgroundColor(resources.getColor(R.color.black))
            }
                is DetailViewState.Empty -> {

                }
            }
        }
    }


}
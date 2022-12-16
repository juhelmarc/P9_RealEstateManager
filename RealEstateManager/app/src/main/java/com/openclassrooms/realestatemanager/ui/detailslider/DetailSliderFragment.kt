package com.openclassrooms.realestatemanager.ui.detailslider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.openclassrooms.realestatemanager.databinding.FragmentDetailBinding
import com.openclassrooms.realestatemanager.databinding.FragmentDetailSliderBinding
import com.openclassrooms.realestatemanager.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailSliderFragment : Fragment() {

    private var _binding: FragmentDetailSliderBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailSliderViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailSliderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.detailLiveData.observe(viewLifecycleOwner) {
            if(it.listPicture != null) {
                var slideModelList: MutableList<SlideModel> = mutableListOf<SlideModel>()
                for (propertyPicture in it.listPicture) {
                    slideModelList.add(SlideModel(propertyPicture.url, propertyPicture.title))
                }
                binding.imageSlider.setImageList(slideModelList, ScaleTypes.CENTER_CROP)

            } else {

            }

        }


    }
}
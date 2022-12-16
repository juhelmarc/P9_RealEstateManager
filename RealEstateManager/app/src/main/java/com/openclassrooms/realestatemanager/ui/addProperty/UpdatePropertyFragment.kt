package com.openclassrooms.realestatemanager.ui.addProperty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.databinding.FragmentAddOrUpdatePropertyBinding
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class UpdatePropertyFragment : Fragment() {

    private val viewModel by viewModels<AddOrUpdatePropertyViewModel>()

    lateinit var binding : FragmentAddOrUpdatePropertyBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddOrUpdatePropertyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.descriptionInput.setText("TESTTTTTTTTTTTTTTTTTTTTTTTTTTTTT")

//        binding.sellingDateInput.setText(formatDate(entryDatePicker.selection))
    }
    fun formatDate(dateMilli: Long?): String {
        var format: String = "MMM dd.yyyy"
        var simpleDateFormat: SimpleDateFormat = SimpleDateFormat(format, Locale.US)
        return simpleDateFormat.format(dateMilli)
    }
}
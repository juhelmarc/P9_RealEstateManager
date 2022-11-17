package com.openclassrooms.realestatemanager.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ListViewModel>()
    //au moment de la creation de la vue on attribu une valeur à notre varriable _binding autre que sa valeur par défaut null
    // (que l'on repassera à null lors de la destruction de la vue)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.root
        val adapter = ListPropertyAdapter {
            //Au click nous transmettons au repository (currentPropertyRepository) via le viewmodel l'id de l'item sur lequel l'utilisateur à cliqué
            //Cet id sera récupéré par le detailFragment via son viewmodel dans le currentPropertyRepository
            viewModel.onItemClicked(it)
        }
        recyclerView.adapter = adapter
        viewModel.propertyListLiveData.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


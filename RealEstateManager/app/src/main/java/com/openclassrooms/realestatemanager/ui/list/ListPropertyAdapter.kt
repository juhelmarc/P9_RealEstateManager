package com.openclassrooms.realestatemanager.ui.list

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.openclassrooms.realestatemanager.R
//import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.ItemPropertyBinding
import dagger.hilt.android.qualifiers.ApplicationContext


class ListPropertyAdapter (
    private val onItemClicked: (id : Long) -> Unit
) : ListAdapter<ListViewState, ListPropertyAdapter.MyViewHolder>(DiffCallback) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(
        ItemPropertyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClicked)
    }

    class MyViewHolder(private val binding: ItemPropertyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ListViewState, onItemClicked: (id: Long) -> Unit) {
            binding.propertyType.text = item.type
            binding.propertyTown.text = item.town
            binding.propertyPrice.text = "$${item.price}"
            binding.propertyPicture.load(item.mainPicture)
            binding.realEstateAgent.text = item.agentName
            if(item.dateOfSale == null || item.dateOfSale == "") {
                binding.available.text = "Available"
                binding.available.setTextColor(ContextCompat.getColor(binding.available.context, R.color.green ))
            } else {
                binding.available.text = "Sold"
                binding.available.setTextColor(ContextCompat.getColor(binding.available.context, R.color.red))
            }
            binding.item.setOnClickListener{
                onItemClicked.invoke(item.id)
            }
        }
    }



    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ListViewState>() {
            override fun areItemsTheSame(oldItem: ListViewState, newItem: ListViewState): Boolean {
                return (oldItem.id == newItem.id )
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: ListViewState, newItem: ListViewState): Boolean {
                return oldItem == newItem
            }
        }
    }


}
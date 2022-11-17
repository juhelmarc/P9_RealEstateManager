package com.openclassrooms.realestatemanager.ui.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
//import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.ItemPropertyBinding


class ListPropertyAdapter (
    private val onItemClicked: (id : String) -> Unit
) : ListAdapter<ListViewState, ListPropertyAdapter.MyViewHolder>(DiffCallback) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(
        ItemPropertyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClicked)
    }

    class MyViewHolder(private val binding: ItemPropertyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ListViewState, onItemClicked: (id: String) -> Unit) {
            binding.propertyType.text = item.type
            binding.propertyTown.text = item.district
            binding.propertyPrice.text = "$${item.price}"
            binding.propertyPicture.load(item.mainPicture)
            binding.item.setOnClickListener{
                //todo -> invoke c'est quoi
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
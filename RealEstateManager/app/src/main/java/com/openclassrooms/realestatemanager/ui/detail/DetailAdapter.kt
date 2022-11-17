package com.openclassrooms.realestatemanager.ui.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.denzcoskun.imageslider.models.SlideModel
import com.openclassrooms.realestatemanager.databinding.ItemDetailBinding



class DetailAdapter(

) : ListAdapter<SlideModel, DetailAdapter.MyViewHolder>(DiffCallback) {
    var onItemClicked1: ((String) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(
        ItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClicked1)
    }

    class MyViewHolder(private val binding: ItemDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SlideModel, onItemClicked: ((String) -> Unit)?) {
            binding.description.text = item.title
            binding.propertyPicture.load(item.imageUrl)
            binding.item.setOnClickListener {
                onItemClicked?.invoke(item.imageUrl!!)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<SlideModel>() {
            override fun areItemsTheSame(oldItem: SlideModel, newItem: SlideModel): Boolean {
                return (oldItem == newItem)
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: SlideModel, newItem: SlideModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
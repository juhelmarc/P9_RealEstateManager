package com.openclassrooms.realestatemanager.ui.formProperty

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.openclassrooms.realestatemanager.data.models.entities.PropertyPictureEntity
import com.openclassrooms.realestatemanager.databinding.ItemDetailBinding

class FormPropertyAdapter() :
    ListAdapter<PropertyPictureEntity, FormPropertyAdapter.MyViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(
        ItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyViewHolder(private val binding: ItemDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PropertyPictureEntity) {
            binding.propertyPicture.load(item.url)
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<PropertyPictureEntity>() {
            override fun areItemsTheSame(
                oldItem: PropertyPictureEntity,
                newItem: PropertyPictureEntity
            ): Boolean {
                return (oldItem == newItem)
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: PropertyPictureEntity,
                newItem: PropertyPictureEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

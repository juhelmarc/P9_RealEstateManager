package com.openclassrooms.realestatemanager.ui.formProperty

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ItemChipBinding


class ChipAdapter(
    private val listener: (Int, Boolean) -> Unit,
) : ListAdapter<FormPropertyViewState.ChipPoiViewState, ChipAdapter.MyViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(
        ItemChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    class MyViewHolder(private val binding: ItemChipBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FormPropertyViewState.ChipPoiViewState, listener: (Int, Boolean) -> Unit) {
            binding.chipItem.tag = item.poiId
            binding.chipItem.setText(item.poiId)
            binding.chipItem.isChecked = item.isSelected
            if(item.isSelected) {
                binding.chipItem.setChipBackgroundColorResource(R.color.purple)
            } else {
                binding.chipItem.setChipBackgroundColorResource(R.color.grey_font)
            }

            binding.chipItem.setOnCheckedChangeListener { buttonView, isChecked ->
                listener(buttonView.tag as Int, isChecked)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<FormPropertyViewState.ChipPoiViewState>() {
            override fun areItemsTheSame(oldItem: FormPropertyViewState.ChipPoiViewState, newItem: FormPropertyViewState.ChipPoiViewState): Boolean {
                return (oldItem == newItem)
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: FormPropertyViewState.ChipPoiViewState, newItem: FormPropertyViewState.ChipPoiViewState): Boolean {
                return oldItem == newItem
            }
        }
    }
}

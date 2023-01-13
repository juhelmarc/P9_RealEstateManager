package com.openclassrooms.realestatemanager.ui.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ItemChipDetailBinding

class DetailChipAdapter (
) : ListAdapter<ChipPoiViewStateDetail, DetailChipAdapter.MyViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(
        ItemChipDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyViewHolder(private val binding: ItemChipDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChipPoiViewStateDetail) {
            binding.chipItemDetail.tag = item.poiId
            binding.chipItemDetail.setText(item.poiId)
            binding.chipItemDetail.isChecked = item.isSelected
            if(item.isSelected) {
                binding.chipItemDetail.setChipBackgroundColorResource(R.color.purple)
            } else {
                binding.chipItemDetail.setChipBackgroundColorResource(R.color.grey_font)
            }

        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ChipPoiViewStateDetail>() {
            override fun areItemsTheSame(oldItem: ChipPoiViewStateDetail, newItem: ChipPoiViewStateDetail): Boolean {
                return (oldItem == newItem)
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: ChipPoiViewStateDetail, newItem: ChipPoiViewStateDetail): Boolean {
                return oldItem == newItem
            }
        }
    }

}
package com.example.bbyak

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bbyak.databinding.ItemNameBinding
import java.util.ArrayList

class NameListAdapter(
    private val dataList: ArrayList<String>
) : RecyclerView.Adapter<NameListAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(private val binding: ItemNameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.profile.text = dataList[position][0].toString()
            binding.name.text = dataList[position]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemNameBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
package com.example.bbyak

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.bbyak.databinding.ItemTableBinding
import java.util.*

class DayTimetableAdapter(
    private val dataList: ArrayList<String>,
    initialTime: String,
    private val width: Int,
    private val height: Int,
    private val isScheduleSaved: Boolean
) : RecyclerView.Adapter<DayTimetableAdapter.ItemViewHolder>() {

    private val selectedTime = initialTime.toCharArray().map { Character.getNumericValue(it) }.toMutableList()

    inner class ItemViewHolder(private val binding: ItemTableBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String, position: Int) {
            val params = LinearLayout.LayoutParams(width, height)
            binding.tvTable.layoutParams = params
            binding.tvTable.text = data
            val time = position - 1
            if (position == 0) {
                binding.tvTable.textSize = 12f
                binding.tvTable.setBackgroundResource(R.drawable.table_cell_enabled)
            } else if (selectedTime[time] == 0) binding.tvTable.setBackgroundResource(R.drawable.table_cell_disabled)
            else if (!isScheduleSaved) binding.tvTable.setBackgroundResource(R.drawable.table_cell_enabled)
            else binding.tvTable.setBackgroundResource(R.drawable.table_cell_selected)

            binding.tvTable.setOnClickListener {
                if (isScheduleSaved || position == 0) return@setOnClickListener
                if (selectedTime[time] == 0) {
                    selectedTime[time] = 1
                    binding.tvTable.setBackgroundResource(R.drawable.table_cell_enabled)
                } else {
                    selectedTime[time] = 0
                    binding.tvTable.setBackgroundResource(R.drawable.table_cell_disabled)
                }
            }
        }
    }

    fun getSelectedTime(): String {
        val result = java.lang.StringBuilder()
        for(i in selectedTime) result.append(i.toString())
        return result.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemTableBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataList[position], position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
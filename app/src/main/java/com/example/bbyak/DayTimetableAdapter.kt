package com.example.bbyak

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.bbyak.databinding.ItemTableBinding
import java.util.*

class DayTimetableAdapter(
    private val dataList: ArrayList<String>,
    private val initialTime: ArrayList<Int>,
    private val width: Int,
    private val height: Int
) : RecyclerView.Adapter<DayTimetableAdapter.ItemViewHolder>() {

    private val selectedTime = ArrayList(initialTime)

    inner class ItemViewHolder(private val binding: ItemTableBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String, position: Int) {
            val params = LinearLayout.LayoutParams(width, height)
            binding.tvTable.layoutParams = params
            binding.tvTable.text = data
            val time = position + 7
            if (position == 0) binding.tvTable.textSize = 12f
            if (selectedTime.contains(time)) binding.tvTable.setBackgroundResource(R.drawable.table_cell_disabled)
            else binding.tvTable.setBackgroundResource(R.drawable.table_cell_enabled)

            binding.tvTable.setOnClickListener {
                if (position == 0) return@setOnClickListener
                if (selectedTime.contains(time)) {
                    selectedTime.remove(time)
                    binding.tvTable.setBackgroundResource(R.drawable.table_cell_enabled)
                } else {
                    selectedTime.add(time)
                    binding.tvTable.setBackgroundResource(R.drawable.table_cell_disabled)
                }
            }
        }
    }

    fun getSelectedTime(): ArrayList<Int>{
        return selectedTime
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
package com.example.bbyak

import android.icu.text.ListFormatter.Width
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.bbyak.databinding.ItemTableBinding

class TimetableAdapter(
    private val dataList: ArrayList<String>,
    private val width: Int,
    private val height: Int
) : RecyclerView.Adapter<TimetableAdapter.ItemViewHolder>() {

    //시간, 요일
    private val selectedTime = ArrayList<Pair<Int, Int>>()

    inner class ItemViewHolder(private val binding: ItemTableBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String, position: Int) {
            val params = LinearLayout.LayoutParams(width, height)
            binding.tvTable.layoutParams = params
            binding.tvTable.text = data
            val dayOfWeek = position % 8
            val time = position / 8 + 7
            binding.tvTable.setOnClickListener {
                if (dayOfWeek == 0 || time == 7) return@setOnClickListener
                if (selectedTime.contains(Pair(dayOfWeek, time))) {
                    selectedTime.remove(Pair(dayOfWeek, time))
                    binding.tvTable.setBackgroundResource(R.drawable.table_cell_not_selected)
                } else {
                    selectedTime.add(Pair(dayOfWeek, time))
                    binding.tvTable.setBackgroundResource(R.drawable.table_cell_selected)
                }
            }
        }
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
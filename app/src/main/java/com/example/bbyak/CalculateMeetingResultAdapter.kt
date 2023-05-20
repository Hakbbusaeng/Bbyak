package com.example.bbyak

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bbyak.databinding.ItemCalculateMeetingResultBinding

class CalculateMeetingResultAdapter(
    private val dataList: ArrayList<PossibleTimeZone>,
    private val context: Context,
    private val callback: (Int, Int, Int) -> Unit
) : RecyclerView.Adapter<CalculateMeetingResultAdapter.ItemViewHolder>() {

    var selectedPosition = -1

    inner class ItemViewHolder(private val binding: ItemCalculateMeetingResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvMeetingDateTime.text = "${dataList[position].month}/${dataList[position].day}(화) ${dataList[position].start} - ${dataList[position].end}"
            binding.tvHeadCount.text = "4/4명 가능"
            binding.tvNameList.text = "김ㅇㅇ, 김ㅇㅇ, 김ㅇㅇ, 김ㅇㅇ"

            val isAll = position == 0
            if (isAll) binding.tvHeadCount.setTextColor(
                ContextCompat.getColor(context, R.color.green)
            )
            else binding.tvHeadCount.setTextColor(ContextCompat.getColor(context, R.color.red))

            binding.root.setOnClickListener {
                selectedPosition = position
                callback(dataList[position].year,dataList[position].month,dataList[position].day)
                notifyDataSetChanged()
            }

            if (position == selectedPosition) binding.root.setBackgroundResource(R.drawable.yellow_background_20dp)
            else binding.root.setBackgroundResource(R.drawable.yellow_stroke_white_background_20dp)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemCalculateMeetingResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
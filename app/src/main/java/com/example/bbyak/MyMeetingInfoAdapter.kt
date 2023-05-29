package com.example.bbyak

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bbyak.databinding.ItemMyMeetingInfoBinding
import java.text.DecimalFormat

class MyMeetingInfoAdapter(
    private val dataList: ArrayList<Meeting>,
    private val context: Context
) : RecyclerView.Adapter<MyMeetingInfoAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(private val binding: ItemMyMeetingInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvMeetingName.text = dataList[position].name
            binding.tvMeetingCreator.text = "by ${dataList[position].creator}"
        }
    }
    private fun getAMPM(_time: Int): String {
        var time = _time + 8
        return if (time < 12) "오전 ${DecimalFormat("00").format(time)}:00"
        else {
            time -= 12
            "오후 ${DecimalFormat("00").format(time)}:00"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemMyMeetingInfoBinding.inflate(
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
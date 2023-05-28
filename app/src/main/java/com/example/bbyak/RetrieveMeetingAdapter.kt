package com.example.bbyak

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.bbyak.databinding.ItemInvitedMeetingInfoBinding
import com.example.bbyak.databinding.ItemTableBinding

class RetrieveMeetingAdapter(
    private val dataList: ArrayList<Meeting>,
    private val context: Context
) : RecyclerView.Adapter<RetrieveMeetingAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(private val binding: ItemInvitedMeetingInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvMeetingName.text = dataList[position].name
            binding.tvMeetingCreator.text = "by ${dataList[position].creator}"

            if (!dataList[position].isMaster) binding.tvMaster.visibility = View.GONE

            binding.root.setOnClickListener {
                context.startActivity(
                    Intent(
                        context,
                        CalculateMeetingActivity::class.java
                    ).apply {
                        putExtra("meetingCode", dataList[position].code)
                        putExtra("meetingName", dataList[position].name)
                        putExtra("meetingCreator", dataList[position].creator)
                        putExtra("isManager", dataList[position].isMaster)
                    }
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemInvitedMeetingInfoBinding.inflate(
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
package com.example.bbyak

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
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

            if (!dataList[position].isManager) binding.tvMaster.visibility = View.GONE
            if(dataList[position].isDone) binding.llMeetingInfo.backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.light_grey)
            else binding.llMeetingInfo.backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.light_yellow)

            binding.root.setOnClickListener {
                if (dataList[position].isDone) {
                    Toast.makeText(context, "이미 확정된 뺙입니다.", Toast.LENGTH_SHORT).show()
                } else {
                    context.startActivity(
                        Intent(
                            context,
                            CalculateMeetingActivity::class.java
                        ).apply {
                            putExtra("meetingCode", dataList[position].code)
                            putExtra("meetingName", dataList[position].name)
                            putExtra("meetingCreator", dataList[position].creator)
                            putExtra("isManager", dataList[position].isManager)
                        }
                    )
                }
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
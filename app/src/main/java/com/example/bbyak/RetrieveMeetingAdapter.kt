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
    private val dataList: ArrayList<String>,
    private val context: Context
) : RecyclerView.Adapter<RetrieveMeetingAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(private val binding: ItemInvitedMeetingInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvMeetingName.text = "학술제 회의"
            binding.tvMeetingCreator.text = "by 김뺙뺙"
            binding.tvCreatedDate.text = "2023/05/07 생성"
            val isManager = position % 2 == 0
            if (!isManager) binding.tvMaster.visibility = View.GONE

            binding.root.setOnClickListener {
                context.startActivity(
                    Intent(
                        context,
                        CalculateMeetingActivity::class.java
                    ).apply { putExtra("isManager", isManager) }
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
        return 10
    }
}
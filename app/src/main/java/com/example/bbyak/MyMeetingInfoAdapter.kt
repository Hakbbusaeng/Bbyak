package com.example.bbyak

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.bbyak.databinding.ItemMyMeetingInfoBinding
import com.example.bbyak.databinding.ProfileListBinding
import java.text.DecimalFormat

class MyMeetingInfoAdapter(
    private val dataList: ArrayList<MyMeeting>,
    private val context: Context
) : RecyclerView.Adapter<MyMeetingInfoAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(private val binding: ItemMyMeetingInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val profileListBinding = ProfileListBinding.bind(binding.root)

        fun bind(position: Int) {
            binding.tvMeetingName.text = dataList[position].name
            binding.tvMeetingCreator.text = "by ${dataList[position].creator}"
            binding.tvMeetingTime.text = "${getAMPM(dataList[position].startTime)}-${getAMPM(dataList[position].endTime)}"
            setProfileList(position)
        }

        private fun setProfileList(position: Int){
            val tvList = listOf(
                profileListBinding.profile1,
                profileListBinding.profile2,
                profileListBinding.profile3,
                profileListBinding.profile4,
                profileListBinding.profile5
            )
            val list = dataList[position].participants
            if (list.size > 5) profileListBinding.tvHeadCount.text = "외 ${list.size - 5}명"
            else {
                profileListBinding.tvHeadCount.visibility = View.GONE
                profileListBinding.flProfileList.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                )
            }
            for (i in 0 until 5) {
                if (i < list.size) tvList[i].text = list[i][0].toString()
                else tvList[i].visibility = View.GONE
            }
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
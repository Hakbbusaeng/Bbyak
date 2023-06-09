package com.example.bbyak

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bbyak.databinding.ItemCalculateMeetingResultBinding
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class CalculateMeetingResultAdapter(
    private val dataList: ArrayList<PossibleTimeZone>,
    private val nameList: ArrayList<String>,
    private val context: Context,
    private val callback: (Int, Int, Int) -> Unit,
    private val select: (Int) -> Unit
) : RecyclerView.Adapter<CalculateMeetingResultAdapter.ItemViewHolder>() {

    var selectedPosition = -1

    inner class ItemViewHolder(private val binding: ItemCalculateMeetingResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvMeetingDateTime.text =
                "${dataList[position].month}/${dataList[position].day}" +
                        "(${
                            getDayOfWeek(
                                dataList[position].year,
                                dataList[position].month,
                                dataList[position].day
                            )
                        }) " +
                        "${getAMPM(dataList[position].start)} - ${getAMPM(dataList[position].end)}"
            binding.tvHeadCount.text = "${dataList[position].headCount}/${nameList.size}명 가능"
            binding.tvNameList.text = getNameList(dataList[position].name)

            if (dataList[position].headCount == nameList.size)
                binding.tvHeadCount.setTextColor(ContextCompat.getColor(context, R.color.green))
            else binding.tvHeadCount.setTextColor(ContextCompat.getColor(context, R.color.red))

            binding.root.setOnClickListener {
                selectedPosition = position
                callback(dataList[position].year, dataList[position].month, dataList[position].day)
                select(position)
                notifyDataSetChanged()
            }

            if (position == selectedPosition) binding.root.setBackgroundResource(R.drawable.yellow_background_20dp)
            else binding.root.setBackgroundResource(R.drawable.yellow_stroke_white_background_20dp)
        }
    }

    private fun getNameList(excludeName: String?): String {
        var str = StringBuilder()
        for (i in nameList) {
            if (excludeName != null && i.compareTo(excludeName) == 0) continue
            str.append("$i, ")
        }
        return str.substring(0, str.length - 2)
    }

    private fun getDayOfWeek(year: Int, month: Int, day: Int): String? {
        val cal = Calendar.getInstance().apply { set(year, month-1, day) }
        return when (cal.get(Calendar.DAY_OF_WEEK)) {
            1 -> "일"; 2 -> "월"; 3 -> "화";
            4 -> "수"; 5 -> "목"; 6 -> "금";
            7 -> "토"; else -> null
        }
    }

    private fun getAMPM(_time: Int): String {
        var time = _time + 8
        return if (time in 0..11) "오전 ${DecimalFormat("00").format(time)}:00"
        else {
            time -= 12
            if (time == 0) time = 12
            "오후 ${DecimalFormat("00").format(time)}:00"
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
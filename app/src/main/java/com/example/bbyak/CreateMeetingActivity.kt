package com.example.bbyak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.bbyak.databinding.ActivityCreateMeetingBinding
import java.util.*

class CreateMeetingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateMeetingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateMeetingBinding.inflate(layoutInflater)

        binding.toolbar.title = "뺙 생성하기"

        val curDate = Calendar.getInstance()
        val timeZone = TimeZone.getTimeZone("Asia/Seoul")
        curDate.timeZone = timeZone
        curDate.set(Calendar.DAY_OF_MONTH, curDate.get(Calendar.DAY_OF_MONTH) - 1)
        Log.e(
            "curDate",
            "${curDate.get(Calendar.YEAR)}/${curDate.get(Calendar.MONTH) + 1}/${curDate.get(Calendar.DAY_OF_MONTH)}"
        )
        binding.selectCalendarView.setMinimumDate(curDate)
        binding.btMeetingCreate.setOnClickListener {
            getSelectedDateList()
        }
        setContentView(binding.root)
    }

    private fun getSelectedDateList() {
        for (cal in binding.selectCalendarView.selectedDates) {
            Log.e(
                "get selected date",
                "${cal.get(Calendar.YEAR)}/${cal.get(Calendar.MONTH) + 1}/${cal.get(Calendar.DAY_OF_MONTH)}"
            )
        }

    }
}
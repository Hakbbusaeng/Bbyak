package com.example.bbyak

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.example.bbyak.databinding.FragmentHomeBinding
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val cals = ArrayList<Calendar>()
    private val calDays = ArrayList<CalendarDay>()
    private val curDate by lazy {
        Calendar.getInstance().apply {
            timeZone = TimeZone.getTimeZone("Asia/Seoul")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        initDateList()
        initCalendarDayList()

        setHighlightDate(curDate)

        binding.calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                setHighlightDate(eventDay.calendar)
            }
        })

        return binding.root
    }

    private fun initDateList() {
        //TODO(날짜 데이터 세팅)
        cals.add(Calendar.getInstance().apply { set(2023, 4, 26) })
        cals.add(Calendar.getInstance().apply { set(2023, 4, 27) })
        cals.add(Calendar.getInstance().apply { set(2023, 4, 28) })
        cals.add(Calendar.getInstance().apply { set(2023, 4, 29) })
    }

    private fun initCalendarDayList() {
        calDays.clear()
        for (item in cals) {
            calDays.add(CalendarDay(item).apply {
                backgroundResource = R.drawable.light_yellow_dot
            })
        }
        binding.calendarView.setCalendarDays(calDays)
    }

    private fun setHighlightDate(cal: Calendar) {
        val new = ArrayList(calDays)
        new.add(0, CalendarDay(cal).apply {
            backgroundResource = R.drawable.yellow_dot
        })
        binding.calendarView.setCalendarDays(new)
    }
}
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
    val selected = ArrayList<CalendarDay>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        val curTimeZone = Calendar.getInstance()

        val timeZone = TimeZone.getTimeZone("Asia/Seoul")
        curTimeZone.timeZone = timeZone

        val cur = Calendar.getInstance()
        cur.set(Calendar.YEAR, curTimeZone.get(Calendar.YEAR))
        cur.set(Calendar.MONTH, curTimeZone.get(Calendar.MONTH))
        cur.set(Calendar.DAY_OF_MONTH, curTimeZone.get(Calendar.DAY_OF_MONTH))

        setHighlightDate(cur)

        binding.calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                selected.removeFirstOrNull()
                val clickedDayCalendar = eventDay.calendar
                setHighlightDate(clickedDayCalendar)
            }
        })

        return binding.root
    }

    fun setHighlightDate(cal: Calendar){

        binding.calendarView.setCalendarDays(selected as List<CalendarDay>)
        val cal1 = Calendar.getInstance().apply { set(2023,3,3) }
        val cal2 = Calendar.getInstance().apply { set(2023,3,7) }
        val cal3 = Calendar.getInstance().apply { set(2023,3,15) }
        val cal4 = Calendar.getInstance().apply { set(2023,3,25) }

        val c = ArrayList<CalendarDay>()
        c.add( CalendarDay(cal).apply { backgroundResource = R.drawable.yellow_dot })
        c.add( CalendarDay(cal1).apply { backgroundResource = R.drawable.light_yellow_dot })
        c.add( CalendarDay(cal2).apply { backgroundResource = R.drawable.light_yellow_dot })
        c.add( CalendarDay(cal3).apply { backgroundResource = R.drawable.light_yellow_dot })
        c.add( CalendarDay(cal4).apply { backgroundResource = R.drawable.light_yellow_dot })
        binding.calendarView.setCalendarDays(c as List<CalendarDay>)
    }


}
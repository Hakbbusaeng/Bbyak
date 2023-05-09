package com.example.bbyak

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.example.bbyak.databinding.FragmentSubmitScheduleBinding
import java.util.*


class MyCalendar(
    val year: Int,
    val month: Int,
    val day: Int
) {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        return (year == (o as MyCalendar).year &&
                month == o.month &&
                day == o.day)
    }

    override fun hashCode(): Int {
        return Integer.valueOf(
            year.toString() + month.toString() + day.toString()
        )
    }

    fun convertToCalendar(): Calendar {
        return Calendar.getInstance().apply { set(year, month, day) }
    }
}

class SubmitScheduleFragment : Fragment() {

    private lateinit var binding: FragmentSubmitScheduleBinding

    var width = 0
    var height = 0

    val exampleCal = ArrayList<MyCalendar>()
    val map = HashMap<MyCalendar, ArrayList<Int>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubmitScheduleBinding.inflate(inflater)


        initExampleCal()
        initTableList()
        setEnabledDate()

        //set first page
        val first = exampleCal[0].convertToCalendar()
        binding.selectCalendarView.setDate(first)


        binding.rvSchedule.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                width = binding.rvSchedule.width
                height = binding.rvSchedule.height / 17
                Log.e("size", "width : $width, height: $height")

                setTimeTable(exampleCal[0])
                binding.rvSchedule.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        binding.selectCalendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val clickedCal = eventDay.calendar
                val myCal = clickedCal.convertToMyCalendar()
                if (exampleCal.contains(myCal)) setTimeTable(myCal)
            }
        })

        return binding.root
    }

    private val tableList = ArrayList<String>()

    private fun initTableList() {
        tableList.add("5/3\n(수)")
        for (i in 8..23) {
            tableList.add(i.toString())
        }
    }

    private fun initExampleCal() {
        exampleCal.add(MyCalendar(2023, 4, 3))
        exampleCal.add(MyCalendar(2023, 4, 7))
        exampleCal.add(MyCalendar(2023, 4, 15))
        exampleCal.add(MyCalendar(2023, 4, 25))
        map[exampleCal[0]] = ArrayList<Int>().apply { add(8);add(9);add(10);add(12);add(15); }
        map[exampleCal[1]] = ArrayList<Int>().apply { add(10);add(11);add(12);add(13);add(15); }
        map[exampleCal[2]] = ArrayList<Int>().apply { add(11);add(12);add(13);add(14); }
        map[exampleCal[3]] = ArrayList<Int>().apply { add(8);add(9);add(10);add(14);add(16); }
    }


    private fun setTimeTable(cal: MyCalendar) {
        Log.e(
            "settimetable",
            "${cal.year} ${cal.month} ${cal.day}"
        )
        val layoutManager = LinearLayoutManager(context)
        binding.rvSchedule.layoutManager = layoutManager
        binding.rvSchedule.adapter = DayTimetableAdapter(tableList, map[cal]!!, width, height)
    }

    fun Calendar.convertToMyCalendar(): MyCalendar {
        return MyCalendar(
            get(Calendar.YEAR),
            get(Calendar.MONTH),
            get(Calendar.DAY_OF_MONTH)
        )
    }


    private fun setEnabledDate() {
        val c = ArrayList<CalendarDay>()
        for (i in exampleCal) {
            val cal = Calendar.getInstance().apply { set(i.year, i.month, i.day) }
            c.add(CalendarDay(cal).apply { labelColor = R.color.black })
        }
        binding.selectCalendarView.setCalendarDays(c as List<CalendarDay>)
    }
}
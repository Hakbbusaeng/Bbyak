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


class MyMeeting(
    val year: Int,
    val month: Int,
    val day: Int,
    val name: String,
    val creator: String,
    val startTime: Int,
    val endTime: Int
)


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val meetings = ArrayList<MyMeeting>()
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

        initMeetingList()
        initCalendarDayList()

        Log.e("----------curDate", "${curDate.get(Calendar.YEAR)}/${curDate.get(Calendar.MONTH)}/${curDate.get(Calendar.DAY_OF_MONTH)}/")
        setRecyclerView(curDate)
        setHighlightDate(curDate)

        binding.calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                eventDay.calendar.let {
                    setRecyclerView(it)
                    setHighlightDate(it)
                }
            }
        })

        return binding.root
    }


    private fun setRecyclerView(cal: Calendar){
        binding.tvDate.text = "${cal.get(Calendar.MONTH)+1}월 ${cal.get(Calendar.DAY_OF_MONTH)}일 ${getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK))}요일"
    }

    private fun getDayOfWeek(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            1 -> "일"; 2 -> "월"; 3 -> "화"
            4 -> "수"; 5 -> "목"; 6 -> "금"
            7 -> "토"; else -> ""
        }
    }


    private fun initMeetingList() {
        //TODO(미팅 정보 세팅)
        meetings.add(MyMeeting(2023, 4, 26, "aa", "aaa", 15,20))
        meetings.add(MyMeeting(2023, 4, 27, "bb", "bbb", 20,24))
        meetings.add(MyMeeting(2023, 4, 28, "cc", "ccc", 10,12))
        meetings.add(MyMeeting(2023, 4, 29, "dd", "ddd", 10,13))
        meetings.add(MyMeeting(2023, 5,1, "ee", "eee", 14,17))
        initDateList()
    }

    private fun initDateList() {
        //TODO(날짜 세팅)
        for(item in meetings){
            cals.add(Calendar.getInstance().apply { set(item.year, item.month, item.day) })
        }
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
        val cal2 = Calendar.getInstance().apply { set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)) }
        new.add(0, CalendarDay(cal2).apply {
            backgroundResource = R.drawable.yellow_dot
        })
        binding.calendarView.setCalendarDays(new)
    }
}
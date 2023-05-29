package com.example.bbyak

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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
    val endTime: Int,
    val participants: ArrayList<String>
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

    private val new = ArrayList<MyMeeting>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        initMeetingList()
        initCalendarDayList()

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


    private fun setRecyclerView(cal: Calendar) {
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val dayOfWeek = getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK))

        binding.tvDate.text = "${month + 1}월 ${day}일 ${dayOfWeek}요일"
        //TODO(리사이클러뷰 세팅)
        binding.rvMeeting.layoutManager = LinearLayoutManager(context)
        binding.rvMeeting.adapter = MyMeetingInfoAdapter(getDateMeetingList(year, month, day), requireContext())
    }

    private fun getDayOfWeek(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            1 -> "일"; 2 -> "월"; 3 -> "화"
            4 -> "수"; 5 -> "목"; 6 -> "금"
            7 -> "토"; else -> ""
        }
    }

    private fun getDateMeetingList(year: Int, month: Int, day: Int): ArrayList<MyMeeting>{
        //TODO(날짜에 해당하는 뺙목록 가져오기)
        new.clear()
        meetings.forEach { if(it.year == year && it.month == month && it.day == day) new.add(it) }
        return new
    }

    private fun initMeetingList() {
        //TODO(미팅 정보 세팅)
        //예시 데이터
        val participants = ArrayList<String>().apply {
            add("춘식이");add("라이언");add("어피치");add("튜브")
        }
        meetings.add(MyMeeting(2023, 4, 26, "aa", "aaa", 15, 20, participants))
        meetings.add(MyMeeting(2023, 4, 26, "aa2", "aaa2", 15, 20, participants))
        meetings.add(MyMeeting(2023, 4, 26, "aa3", "aaa3", 15, 20, participants))
        meetings.add(MyMeeting(2023, 4, 26, "aa4", "aaa4", 15, 20, participants))
        meetings.add(MyMeeting(2023, 4, 27, "bb", "bbb", 20, 24, participants))
        meetings.add(MyMeeting(2023, 4, 28, "cc", "ccc", 10, 12, participants))
        meetings.add(MyMeeting(2023, 4, 29, "dd", "ddd", 10, 13, participants))
        meetings.add(MyMeeting(2023, 5, 1, "ee", "eee", 14, 17, participants))
        initDateList()
    }

    private fun initDateList() {
        //TODO(날짜 세팅)
        for (item in meetings) {
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
        val cal2 = Calendar.getInstance().apply {
            set(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
        }
        new.add(0, CalendarDay(cal2).apply {
            backgroundResource = R.drawable.yellow_dot
        })
        binding.calendarView.setCalendarDays(new)
    }
}
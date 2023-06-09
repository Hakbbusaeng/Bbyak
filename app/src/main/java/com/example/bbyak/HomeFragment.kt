package com.example.bbyak

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.example.bbyak.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
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

        val month = curDate.get(Calendar.MONTH)
        val day = curDate.get(Calendar.DAY_OF_MONTH)
        val dayOfWeek = getDayOfWeek(curDate.get(Calendar.DAY_OF_WEEK))

        binding.tvDate.text = "${month + 1}월 ${day}일 ${dayOfWeek}요일"
        setHighlightDate(curDate)

        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            initMeetingList().join()
            binding.progressBar.visibility = View.GONE
            initCalendarDayList()

            setRecyclerView(curDate)
            setHighlightDate(curDate)
        }

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
        binding.rvMeeting.adapter =
            MyMeetingInfoAdapter(getDateMeetingList(year, month, day), requireContext())
    }

    private fun getDayOfWeek(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            1 -> "일"; 2 -> "월"; 3 -> "화"
            4 -> "수"; 5 -> "목"; 6 -> "금"
            7 -> "토"; else -> ""
        }
    }

    private fun getDateMeetingList(year: Int, month: Int, day: Int): ArrayList<MyMeeting> {
        //TODO(날짜에 해당하는 뺙목록 가져오기)
        new.clear()
        meetings.forEach { if (it.year == year && it.month == month && it.day == day) new.add(it) }
        return new
    }

    private fun initMeetingList() =
        //TODO(미팅 정보 세팅)
        lifecycleScope.launch {
            val bbyakList = getUserBbyak(getUid())

            for (bbyak in bbyakList) {
                meetings.add(bbyak)
            }
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
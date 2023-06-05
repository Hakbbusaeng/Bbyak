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
    var dayOfWeek: String = ""
    var schedule: String = ""

    fun convertToCalendar(): Calendar {
        return Calendar.getInstance().apply { set(year, month, day) }
    }

    override fun toString(): String = "$year/$month/$day"
}

class SubmitScheduleFragment : Fragment() {

    private lateinit var binding: FragmentSubmitScheduleBinding

    var width = 0
    var height = 0

    val cals = ArrayList<MyCalendar>()

    var meetingCode: String? = null

    var currentCal: MyCalendar? = null

    private val curDate by lazy {
        Calendar.getInstance().apply {
            timeZone = TimeZone.getTimeZone("Asia/Seoul")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubmitScheduleBinding.inflate(inflater)

        meetingCode = arguments?.getString("meetingCode")
        val meetingName = arguments?.getString("meetingName")
        val meetingCreator = arguments?.getString("meetingCreator")
        binding.tvMeetingName.text = "$meetingName by $meetingCreator"

        getCalendar()
        setTableList()
        setEnabledDate()

        //set first page
        currentCal = cals[0]
        val first = cals[0].convertToCalendar()
        first.set(Calendar.MONTH, first.get(Calendar.MONTH) - 1)
        binding.selectCalendarView.setDate(first)


        binding.rvSchedule.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                width = binding.rvSchedule.width
                height = binding.rvSchedule.height / 17
                Log.e("size", "width : $width, height: $height")

                setTimeTable(cals[0])
                binding.rvSchedule.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        binding.selectCalendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                currentCal?.let {
                    setMyCalendarSchedule( it )
                }
                val clickedCal = eventDay.calendar
                val myCal = getMyCal(
                    clickedCal.get(Calendar.YEAR),
                    clickedCal.get(Calendar.MONTH) + 1,
                    clickedCal.get(Calendar.DAY_OF_MONTH)
                )
                currentCal = myCal
                currentCal?.let { setTimeTable(it) }
            }
        })

        return binding.root
    }

    private val tableList = ArrayList<String>()

    private fun setTableList() {
        tableList.add("${cals[0].month}/${cals[0].day}(${cals[0].dayOfWeek})")
        for (i in 8..23) {
            tableList.add(i.toString())
        }
    }

    private fun getCalendar() {
        //TODO(날짜 세팅) meetingCode 이용
        val dateList = getMeetingDate(meetingCode.toString())
        for (date in dateList) {
            cals.add(MyCalendar(date.first, date.second, date.third))
        }

        //요일, 스케줄 세팅
        for (item in cals) {
            item.dayOfWeek = getDayOfWeek(item.year, item.month, item.day)
            item.schedule = getSchedule(item.dayOfWeek)
        }
    }

    private fun getSchedule(str: String): String {
        //TODO(요일별 스케줄 가져오기)
        val sch = getUserSchedule()
        return when (str) {
            "월" -> sch[0]
            "화" -> sch[1]
            "수" -> sch[2]
            "목" -> sch[3]
            "금" -> sch[4]
            "토" -> sch[5]
            "일" -> sch[6]
            else -> ""
        }
    }

    private fun getDayOfWeek(year: Int, month: Int, day: Int): String {
        val cal = Calendar.getInstance().apply { set(year, month - 1, day) }
        return when (cal.get(Calendar.DAY_OF_WEEK)) {
            1 -> "일"; 2 -> "월"; 3 -> "화"
            4 -> "수"; 5 -> "목"; 6 -> "금"
            7 -> "토"; else -> ""
        }
    }

    private lateinit var adapter: DayTimetableAdapter

    private fun setTimeTable(cal: MyCalendar) {
        Log.e(
            "settimetable",
            "${cal.year} ${cal.month} ${cal.day}"
        )
        tableList[0] = "${cal.month}/${cal.day}(${cal.dayOfWeek}}"
        val layoutManager = LinearLayoutManager(context)
        binding.rvSchedule.layoutManager = layoutManager
        adapter = DayTimetableAdapter(
            tableList, cal.schedule, width, height,
            (activity as CalculateMeetingActivity).isScheduleSaved
        )
        binding.rvSchedule.adapter = adapter
    }

    private fun getMyCal(year: Int, month: Int, day: Int): MyCalendar? {
        for (i in cals) {
            if (i.year == year && i.month == month && i.day == day) return i
        }
        return null
    }


    private fun setEnabledDate() {
        val c = ArrayList<CalendarDay>()
        c.add(CalendarDay(curDate).apply { labelColor = R.color.slight_light_grey })
        for (i in cals) {
            val cal = Calendar.getInstance().apply { set(i.year, i.month - 1, i.day) }
            c.add(CalendarDay(cal).apply { labelColor = R.color.black })
        }
        binding.selectCalendarView.setCalendarDays(c as List<CalendarDay>)
    }

    fun setMyCalendarSchedule(cal: MyCalendar?) {
        cal?.let { it.schedule = adapter.getSelectedTime() }
    }

    fun refreshTimeTable(){
        currentCal?.let { setMyCalendarSchedule(it) }
        currentCal?.let { setTimeTable(it) }
    }

    fun getMySchedule(): ArrayList<MyCalendar>{
        currentCal?.let { setMyCalendarSchedule(it) }
        return cals
    }

}
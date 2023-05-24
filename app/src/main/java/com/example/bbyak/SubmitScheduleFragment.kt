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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubmitScheduleBinding.inflate(inflater)

        getCalendar()
        setTableList()
        setEnabledDate()

        meetingCode = arguments?.getString("meetingCode")
        val meetingName = arguments?.getString("meetingName")
        val meetingCreator = arguments?.getString("meetingCreator")
        binding.tvMeetingName.text = "$meetingName by $meetingCreator"

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
        cals.add(MyCalendar(2023, 5, 3))
        cals.add(MyCalendar(2023, 5, 7))
        cals.add(MyCalendar(2023, 5, 15))
        cals.add(MyCalendar(2023, 5, 25))

        //요일, 스케줄 세팅
        for (item in cals) {
            item.dayOfWeek = getDayOfWeek(item.year, item.month, item.day)
            item.schedule = getSchedule(item.dayOfWeek)
        }
    }

    private fun getSchedule(str: String): String {
        //TODO(요일별 스케줄 가져오기) 예시 데이터
        return when (str) {
            "월" -> "0010110101011101"
            "화" -> "1010111011010111"
            "수" -> "1010100001001011"
            "목" -> "1111110000000000"
            "금" -> "1101011111000000"
            "토" -> "1010111011010111"
            "일" -> "1101011111000000"
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
        for (i in cals) {
            val cal = Calendar.getInstance().apply { set(i.year, i.month - 1, i.day) }
            c.add(CalendarDay(cal).apply { labelColor = R.color.black })
        }
        binding.selectCalendarView.setCalendarDays(c as List<CalendarDay>)
    }

    fun setMyCalendarSchedule(cal: MyCalendar?) {
        cal?.let { cal.schedule = adapter.getSelectedTime() }
    }

    fun refreshTimeTable(){
        currentCal?.let { setMyCalendarSchedule(it) }
        currentCal?.let { setTimeTable(it) }
    }

}
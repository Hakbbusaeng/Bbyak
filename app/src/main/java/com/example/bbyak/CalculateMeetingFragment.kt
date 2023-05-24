package com.example.bbyak

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.applandeo.materialcalendarview.CalendarDay
import com.example.bbyak.databinding.FragmentCalculateMeetingBinding
import java.util.*

class CalculateMeetingFragment : Fragment() {

    private lateinit var binding: FragmentCalculateMeetingBinding

    private lateinit var possibleTimes: ArrayList<PossibleTimeZone>

    private val calDays = ArrayList<CalendarDay>()

    private lateinit var users: ArrayList<User>
    private val userNames by lazy { ArrayList<String>() }

    private var meetingCode: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalculateMeetingBinding.inflate(layoutInflater)

        meetingCode = arguments?.getString("meetingCode")

        //유저 스케줄 정보 세팅
        initUserScheduleList()

        //가능한 날짜 계산
        possibleTimes = getPossibleTime(users)

        //캘린더 초기화
        initCalendarDayList()

        //리사이클러뷰 세팅
        setRecyclerView()

        return binding.root
    }

    private fun initUserScheduleList() {

        //TODO(제출한 유저 스케줄 users에 세팅) meetingCode 사용

        //예시 데이터
        val exTime = "1111111111111111"
        val exTime2 = "1111111100000000"
        val exSchedule =
            ArrayList<Schedule>().apply {
                add(Schedule(2023, 5, 18, exTime))
                add(Schedule(2023, 5, 19, exTime))
                add(Schedule(2023, 5, 20, exTime))
                add(Schedule(2023, 5, 21, exTime))
            }
        val exSchedule2 =
            ArrayList<Schedule>().apply {
                add(Schedule(2023, 5, 18, exTime2))
                add(Schedule(2023, 5, 19, exTime2))
                add(Schedule(2023, 5, 20, exTime2))
                add(Schedule(2023, 5, 21, exTime2))
            }
        users = ArrayList<User>().apply {
            add(User("AAA", exSchedule))
            add(User("BBB", exSchedule))
            add(User("CCC", exSchedule))
            add(User("DDD", exSchedule))
            add(User("EEE", exSchedule))
            add(User("FFF", exSchedule))
            add(User("GGG", exSchedule))
            add(User("HHH", exSchedule2))
        }
        for (item in users) userNames.add(item.name)
    }

    private fun setRecyclerView() {
        binding.rvCalculateMeetingResult.layoutManager = LinearLayoutManager(context)
        binding.rvCalculateMeetingResult.adapter =
            CalculateMeetingResultAdapter(
                possibleTimes,
                userNames,
                requireContext(),
                { y, m, d -> setHighlightDate(y, m, d) },
                { pos -> selectTimeZone(pos) })
    }

    private fun initCalendarDayList() {
        calDays.clear()
        for (item in possibleTimes) {
            val cal = Calendar.getInstance().apply { set(item.year, item.month - 1, item.day) }
            calDays.add(CalendarDay(cal).apply {
                backgroundResource = R.drawable.deep_yellow_empty_dot
            })
        }
        binding.calendarView.setCalendarDays(calDays)
    }

    private fun setHighlightDate(year: Int, month: Int, day: Int) {
        val new = ArrayList(calDays)
        val cal = Calendar.getInstance().apply { set(year, month - 1, day) }
        new.add(0, CalendarDay(cal).apply {
            backgroundResource = R.drawable.deep_yellow_dot
        })
        binding.calendarView.setCalendarDays(new)
    }

    private var selectedTimeZone: PossibleTimeZone? = null
    private fun selectTimeZone(position: Int) {
        selectedTimeZone = possibleTimes[position]
    }

    fun getSelectedTimeZone(): PossibleTimeZone?{
        return selectedTimeZone
    }
}

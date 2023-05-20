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


class PossibleTimeZone(
    val year: Int,
    val month: Int,
    val day: Int,
    val start: Int,
    var end: Int
)

class CalculateMeetingFragment : Fragment() {

    private lateinit var binding: FragmentCalculateMeetingBinding

    private val possibleTimes by lazy { ArrayList<PossibleTimeZone>() }

    private val calDays = ArrayList<CalendarDay>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalculateMeetingBinding.inflate(layoutInflater)

        //계산결과 날짜목록 초기화
        initPossibleTimeList()

        //캘린더 초기화
        initCalendarDayList()

        //리사이클러뷰 세팅
        setRecyclerView()

        return binding.root
    }

    private fun initPossibleTimeList() {
        //example list
        possibleTimes.add(PossibleTimeZone(2023, 5, 4, 1, 5))
        possibleTimes.add(PossibleTimeZone(2023, 5, 4, 6, 8))
        possibleTimes.add(PossibleTimeZone(2023, 5, 5, 7, 9))
        possibleTimes.add(PossibleTimeZone(2023, 5, 6, 4, 6))
        possibleTimes.add(PossibleTimeZone(2023, 5, 6, 4, 6))
        possibleTimes.add(PossibleTimeZone(2023, 5, 10, 4, 6))
    }

    private fun setRecyclerView() {
        binding.rvCalculateMeetingResult.layoutManager = LinearLayoutManager(context)
        binding.rvCalculateMeetingResult.adapter =
            CalculateMeetingResultAdapter(
                possibleTimes,
                requireContext()
            ) { y, m, d -> setHighlightDate(y, m, d) }
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
}

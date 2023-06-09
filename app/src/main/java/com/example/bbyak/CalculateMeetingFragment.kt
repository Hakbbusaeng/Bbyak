package com.example.bbyak

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.applandeo.materialcalendarview.CalendarDay
import com.example.bbyak.databinding.FragmentCalculateMeetingBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

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

        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            //유저 스케줄 정보 세팅
            initUserScheduleList().join()

            withContext(Dispatchers.Main) {
                //가능한 날짜 계산
                possibleTimes = getPossibleTime(users)
            }
            //캘린더 초기화
            initCalendarDayList()

            //리사이클러뷰 세팅
            setRecyclerView()
            
            binding.progressBar.visibility = View.GONE
        }

        return binding.root
    }

    private suspend fun initUserScheduleList() =
        lifecycleScope.launch {

            val submitUserList = getSubmitUserList(meetingCode.toString())
            val dateList = getMeetingDate(meetingCode.toString())

            users = ArrayList<User>()

            for (uid in submitUserList) {
                val timeList = getUserTime(uid, meetingCode.toString())
                val userScheduleList = ArrayList<Schedule>()

                var i = 0
                for (date in dateList) {
                    userScheduleList.apply {
                        add(Schedule(date.first, date.second, date.third, timeList[i]))
                    }
                    i += 1
                }

                println("userScheduleList: $userScheduleList")
                users.apply {
                    add(User(getUserName(uid), userScheduleList))
                }
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

    fun getSelectedTimeZone(): PossibleTimeZone? {
        return selectedTimeZone
    }
}

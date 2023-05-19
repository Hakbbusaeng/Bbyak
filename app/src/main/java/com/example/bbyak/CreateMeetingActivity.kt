package com.example.bbyak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserHandle
import android.util.Log
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.bbyak.databinding.ActivityCreateMeetingBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class CreateMeetingActivity : AppCompatActivity() {

    private val FRAGMENT_CREATE_MEETING = 1
    private val FRAGMENT_FINISH_CREATE_MEETING = 2

    private lateinit var binding: ActivityCreateMeetingBinding
    private lateinit var cmFragment: CreateMeetingFragment
    private lateinit var fcmFragment: FinishCreateMeetingFragment

    private lateinit var meetingCode: String

    private val database = Firebase.database
    private val meeting = database.getReference("meeting")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateMeetingBinding.inflate(layoutInflater)

        binding.toolbar.title = "뺙 생성하기"

        switchFragment(FRAGMENT_CREATE_MEETING)
        binding.btConfirm.setOnClickListener { switchFragment(FRAGMENT_FINISH_CREATE_MEETING) }

        setContentView(binding.root)
    }

    private fun switchFragment(fragment: Int) {
        when (fragment) {
            FRAGMENT_CREATE_MEETING -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    cmFragment = CreateMeetingFragment()
                    replace(binding.fragmentContainer.id, cmFragment)
                    binding.btConfirm.text = "생성하기"
                }
            }
            FRAGMENT_FINISH_CREATE_MEETING -> {
                createMeeting()
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    fcmFragment = FinishCreateMeetingFragment()
                    fcmFragment.arguments = Bundle().apply { putString("code", meetingCode) }
                    replace(binding.fragmentContainer.id, fcmFragment)
                    binding.btConfirm.text = "완료"
                    binding.btConfirm.setOnClickListener { finish() }
                }
            }
        }
    }

    data class Date(val year: Int, val month: Int, val day: Int)
    data class User(val id: String, val date: List<Date>, val time:List<Boolean>)
    data class Meeting(val id: String, val name: String, val date: List<Date>, val user: List<User>?)   //user: 나중에 수정하기

    private fun createMeeting() {
        // Meeting Id
        val key = meeting.push().key.toString()
        Log.e("meeting key", key)

        // Meeting Name
        val name = cmFragment.binding.editTextMeetingName.text.toString()
        Log.e("meeting name", name)

        // Meeting Date
        val date = ArrayList<Date>()
        for (i in cmFragment.binding.selectCalendarView.selectedDates) {
            val year = i.get(Calendar.YEAR)
            val month = i.get(Calendar.MONTH) + 1
            val day = i.get(Calendar.DAY_OF_MONTH)
            val ymd = Date(year, month, day)

            Log.e(
                "selected date", year.toString() + "/" + month.toString() + "/"+ day.toString()
            )

            date.add(ymd)
        }

        // Add Meeting
        val newMeeting = Meeting(key, name, date, null)
        meeting.child(key).setValue(newMeeting)

        //get code
        meetingCode = key
    }
}
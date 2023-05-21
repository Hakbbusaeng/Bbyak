package com.example.bbyak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserHandle
import android.util.Log
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.bbyak.databinding.ActivityCreateMeetingBinding
import com.google.firebase.auth.ktx.auth
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

    private val user = Firebase.auth.currentUser
    private val database = Firebase.database
    private val meetings = database.getReference("Meetings")
    private val users = database.getReference("Users")

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
    data class User(var email: String, val date: List<Date>?, val time: List<List<Boolean>>?, val master: Boolean)
    data class Meeting(val id: String, val name: String, val date: List<Date>, val user: List<User>)   //user: 나중에 수정하기

    private fun createMeeting() {
        // Meeting Key
        val key = meetings.push().key.toString()
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

        // Meeting User(master)
        val userList = ArrayList<User>()
        var master = User("",null, null, true)
        user?.let {
            val email = it.email.toString()
            master.email = email
        }
        userList.add(master)

        // Add Meeting
        val newMeeting = Meeting(key, name, date, userList)
        meetings.child(key).setValue(newMeeting)

        // Add User Data
        user?.let {
            users.child(it.uid).child("meetings").push().setValue(key)
        }

        //get code
        meetingCode = key
    }
}
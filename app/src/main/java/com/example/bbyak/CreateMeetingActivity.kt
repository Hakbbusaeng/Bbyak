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

    private fun createMeeting() {
        // Meeting Id
        val mId = meetingsRef.push().key.toString()
        Log.e("meeting key", mId)

        // Meeting Name
        val mName = cmFragment.binding.editTextMeetingName.text.toString()
        Log.e("meeting name", mName)

        // Meeting Date
        val mDate = ArrayList<String>()
        for (i in cmFragment.binding.selectCalendarView.selectedDates) {
            val year = i.get(Calendar.YEAR)
            val month = i.get(Calendar.MONTH) + 1
            val day = i.get(Calendar.DAY_OF_MONTH)
            val ymd = year.toString() + "/" + month.toString() + "/"+ day.toString()

            Log.e("selected date", ymd)

            mDate.add(ymd)
        }

        // Meeting User(master)
        val time = ArrayList<String>()
        for (i in mDate) {
            time.add("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0")
        }
        var master = newUser(getUid(), getUserName(), time, true)

        // Add Meeting
        val newMeeting = newMeeting(mId, mName, mDate, getUserName(), false)
        meetingsRef.child(mId).setValue(newMeeting)
        meetingsRef.child(mId).child("user").child(getUid()).setValue(master)

        // Add User Data
        usersRef.child(getUid()).child("meeting").child(mId).setValue(mId)

        //get code
        meetingCode = mId
    }
}
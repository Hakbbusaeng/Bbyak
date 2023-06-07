package com.example.bbyak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.bbyak.databinding.ActivityCreateMeetingBinding
import kotlinx.coroutines.launch
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
                lifecycleScope.launch {
                    binding.progressBar.visibility = View.VISIBLE
                    createMeeting().join()
                    binding.progressBar.visibility = View.GONE
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
    }

    private fun createMeeting() =
        lifecycleScope.launch {
            // Meeting Id
            val code = meetingsRef.push().key.toString()
            Log.e("meeting key", code)

            // Meeting Name
            val mName = cmFragment.binding.editTextMeetingName.text.toString()
            Log.e("meeting name", mName)

            // Meeting Date
            val mDate = ArrayList<String>()
            for (i in cmFragment.binding.selectCalendarView.selectedDates) {
                val year = i.get(Calendar.YEAR)
                val month = i.get(Calendar.MONTH) + 1
                val day = i.get(Calendar.DAY_OF_MONTH)
                val ymd = year.toString() + "/" + month.toString() + "/" + day.toString()

                Log.e("selected date", ymd)

                mDate.add(ymd)
            }

            // Meeting User(master)
            val time = ArrayList<String>()
            for (i in mDate) {
                time.add("1111111111111111")
            }
            val user = mUser(getUid(), getUserName(getUid()), true)
            val meeting = uMeeting(code, time, false)

            // Add Meeting
            val newMeeting = newMeeting(code, mName, mDate, getUserName(getUid()), false)
            meetingsRef.child(code).setValue(newMeeting)
            meetingsRef.child(code).child("user").child(getUid()).setValue(user)

            // Add User Data
            usersRef.child(getUid()).child("meeting").child(code).setValue(meeting)

            //get code
            meetingCode = code
        }
}
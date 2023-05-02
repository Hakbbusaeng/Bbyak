package com.example.bbyak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.bbyak.databinding.ActivityCreateMeetingBinding
import java.util.*

class CreateMeetingActivity : AppCompatActivity() {

    private val FRAGMENT_CREATE_MEETING = 1
    private val FRAGMENT_FINISH_CREATE_MEETING = 2

    private lateinit var binding: ActivityCreateMeetingBinding
    private lateinit var cmFragment: CreateMeetingFragment

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
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<FinishCreateMeetingFragment>(binding.fragmentContainer.id)
                    binding.btConfirm.text = "완료"
                    binding.btConfirm.setOnClickListener { finish() }
                }
                createMeeting()
            }
        }
    }

    private fun createMeeting() {
        Log.e("meeting name", cmFragment.binding.editTextMeetingName.text.toString())
        Log.e("min headcount", cmFragment.binding.editTextMinHeadCount.text.toString())
        for (i in cmFragment.binding.selectCalendarView.selectedDates) {
            Log.e(
                "selected date",
                "${i.get(Calendar.YEAR)}/${i.get(Calendar.MONTH) + 1}/${i.get(Calendar.DAY_OF_MONTH)}"
            )
        }
    }
}
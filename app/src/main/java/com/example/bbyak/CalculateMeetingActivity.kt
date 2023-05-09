package com.example.bbyak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.bbyak.databinding.ActivityCalculateMeetingBinding

class CalculateMeetingActivity : AppCompatActivity() {

    private val FRAGMENT_SUBMIT_SCHEDULE = 1
    private lateinit var binding: ActivityCalculateMeetingBinding
    private lateinit var ssFragment: SubmitScheduleFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalculateMeetingBinding.inflate(layoutInflater)
        binding.toolbar.title = "내 스케줄 제출"

        switchFragment(FRAGMENT_SUBMIT_SCHEDULE)

        setContentView(binding.root)
    }

    private fun switchFragment(fragment: Int){
        when(fragment) {
            FRAGMENT_SUBMIT_SCHEDULE -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    ssFragment = SubmitScheduleFragment()
                    replace(binding.fragmentContainer.id, ssFragment)
                    binding.btConfirm.text = "제출하기"
                }
            }
        }
    }
}
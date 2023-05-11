package com.example.bbyak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.bbyak.databinding.ActivityCalculateMeetingBinding

class CalculateMeetingActivity : AppCompatActivity() {

    private val FRAGMENT_SUBMIT_SCHEDULE = 1
    private val FRAGMENT_CALCULATE_SCHEDULE = 2

    private lateinit var binding: ActivityCalculateMeetingBinding
    private lateinit var ssFragment: SubmitScheduleFragment

    private lateinit var btManage: MenuItem
    private var isManager = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalculateMeetingBinding.inflate(layoutInflater)
        binding.toolbar.title = "내 스케줄 제출"
        setSupportActionBar(binding.toolbar)

        isManager = intent.getBooleanExtra("isManager", false)

        switchFragment(FRAGMENT_SUBMIT_SCHEDULE)

        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.manage_menu, menu)
        btManage = menu.findItem(R.id.item_manage)
        btManage.isVisible = isManager
        return true
    }

    private fun switchFragment(fragment: Int) {
        when (fragment) {
            FRAGMENT_SUBMIT_SCHEDULE -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    ssFragment = SubmitScheduleFragment()
                    replace(binding.fragmentContainer.id, ssFragment)
                    binding.btConfirm.text = "제출하기"
                }
            }
            FRAGMENT_CALCULATE_SCHEDULE -> {
                btManage.isVisible = false
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            }
        }
    }
}
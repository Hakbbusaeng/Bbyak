package com.example.bbyak

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.bbyak.databinding.ActivityCalculateMeetingBinding

class CalculateMeetingActivity : AppCompatActivity() {

    private val FRAGMENT_SUBMIT_SCHEDULE = 1
    private val FRAGMENT_BEFORE_CALCULATE = 2
    private val FRAGMENT_CALCULATE_MEETING = 3

    private lateinit var binding: ActivityCalculateMeetingBinding
    private lateinit var ssFragment: SubmitScheduleFragment
    private lateinit var bcFragment: BeforeCalculateFragment
    private lateinit var cmFragment: CalculateMeetingFragment

    private lateinit var menuManage: MenuItem

    private var meetingCode: String? = null
    private var meetingName: String? = null
    private var meetingCreator: String? = null
    private var isManager = false
    private var showMenu = false

    private var currentFragment = FRAGMENT_SUBMIT_SCHEDULE
    var isScheduleSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalculateMeetingBinding.inflate(layoutInflater)
        binding.toolbar.title = "내 스케줄 제출"
        setSupportActionBar(binding.toolbar)

        meetingCode = intent.getStringExtra("meetingCode")
        meetingName = intent.getStringExtra("meetingName")
        meetingCreator = intent.getStringExtra("meetingCreator")
        isManager = intent.getBooleanExtra("isManager", false)

        //TODO(이미 제출했는지 여부 설정)
        //isScheduleSaved = true or false

        switchFragment(currentFragment)

        binding.btConfirm.setOnClickListener {
            when (currentFragment) {
                FRAGMENT_SUBMIT_SCHEDULE->{
                    if (!isScheduleSaved) binding.btConfirm.text = "수정하기"
                    else binding.btConfirm.text = "제출하기"
                    isScheduleSaved = !isScheduleSaved
                    ssFragment.refreshTimeTable()
                }
                FRAGMENT_BEFORE_CALCULATE -> {
                    switchFragment(FRAGMENT_CALCULATE_MEETING)
                }
                FRAGMENT_CALCULATE_MEETING -> {
                    //TODO(미팅 날짜 확정)
                }
            }
        }

        setContentView(binding.root)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuManage = it.findItem(R.id.item_manage)
            menuManage.isVisible = showMenu
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.manage_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_manage -> {
                if (!isScheduleSaved) Toast.makeText(this, "스케줄을 제출해 주세요", Toast.LENGTH_SHORT)
                    .show()
                else {
                    currentFragment = FRAGMENT_BEFORE_CALCULATE
                    switchFragment(currentFragment)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun switchFragment(fragment: Int) {
        currentFragment = fragment
        when (fragment) {
            FRAGMENT_SUBMIT_SCHEDULE -> {
                binding.toolbar.title = "내 스케줄 제출"
                showMenu = isManager
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    ssFragment = SubmitScheduleFragment()
                    ssFragment.arguments = Bundle().apply {
                        putString("meetingCode", meetingCode)
                        putString("meetingName", meetingName)
                        putString("meetingCreator", meetingCreator)
                    }
                    replace(binding.fragmentContainer.id, ssFragment)
                }
                if (!isScheduleSaved) binding.btConfirm.text = "제출하기"
                else binding.btConfirm.text = "수정하기"
                binding.ivGradient.visibility = View.GONE
            }
            FRAGMENT_BEFORE_CALCULATE -> {
                binding.toolbar.title = meetingName
                showMenu = false
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    bcFragment = BeforeCalculateFragment()
                    replace(binding.fragmentContainer.id, bcFragment)
                }
                binding.btConfirm.text = "날짜 계산하기"
                binding.ivGradient.visibility = View.GONE
            }
            FRAGMENT_CALCULATE_MEETING -> {
                binding.toolbar.title = meetingName
                showMenu = false
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    cmFragment = CalculateMeetingFragment()
                    replace(binding.fragmentContainer.id, cmFragment)
                }
                binding.btConfirm.text = "확정하기"
                binding.ivGradient.visibility = View.VISIBLE
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        when (currentFragment) {
            FRAGMENT_SUBMIT_SCHEDULE -> finish()
            FRAGMENT_BEFORE_CALCULATE -> switchFragment(FRAGMENT_SUBMIT_SCHEDULE)
            FRAGMENT_CALCULATE_MEETING -> switchFragment(FRAGMENT_BEFORE_CALCULATE)
        }
    }
}
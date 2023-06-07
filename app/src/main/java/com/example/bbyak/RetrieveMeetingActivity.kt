package com.example.bbyak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bbyak.databinding.ActivityRetrieveMeetingBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RetrieveMeetingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRetrieveMeetingBinding

    private val meetings = ArrayList<Meeting>()

    private lateinit var adapter: RetrieveMeetingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRetrieveMeetingBinding.inflate(layoutInflater)

        binding.toolbar.title = "뺙 조회하기"

        binding.btAddMeeting.setOnClickListener {
            val code = binding.etMeetingName.text.toString()
            val meetingList = getMeetingList()

            if (binding.etMeetingName.text.equals("")) Toast.makeText(
                this,
                "코드를 입력해주세요.",
                Toast.LENGTH_SHORT
            ).show()
            else if (code !in meetingList) Toast.makeText(this, "잘못된 코드입니다.", Toast.LENGTH_SHORT)
                .show()
            else {
                //TODO(미팅에 유저 추가)
                val time = ArrayList<String>()
                val mDate = getMeetingDate(code)
                for (i in mDate) {
                    time.add("1111111111111111")
                }
                val user = mUser(getUid(), getUserName(getUid()), false)
                val meeting = uMeeting(code, time, false)

                meetingsRef.child(code).child("user").child(getUid()).setValue(user)
                usersRef.child(getUid()).child("meeting").child(code).setValue(meeting)

                lifecycleScope.launch {
                    binding.progressBar.visibility = View.VISIBLE
                    getInvitedMeetingList().join()
                    binding.etMeetingName.text.clear()
                    adapter.notifyDataSetChanged()
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            getInvitedMeetingList().join()
            setRecyclerView()
            binding.progressBar.visibility = View.GONE
        }

        setContentView(binding.root)
    }

    private fun getInvitedMeetingList() =
        //TODO(뺙 목록 가져오기)
        lifecycleScope.launch {
            val meetingList = getUserMeeting()
            meetings.clear()
            for (code in meetingList) {
                val meeting = getMeeting(code)
                meetings.add(meeting)
            }
            meetings.reverse()

        }

    private fun setRecyclerView() {
        binding.rvRetrieveMeeting.layoutManager = LinearLayoutManager(this)
        adapter = RetrieveMeetingAdapter(meetings, this)
        binding.rvRetrieveMeeting.adapter = adapter
    }
}

class Meeting(
    var code: String,
    var name: String,
    var creator: String,
    var isManager: Boolean,
    var isDone: Boolean
)


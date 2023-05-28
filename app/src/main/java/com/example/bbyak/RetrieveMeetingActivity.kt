package com.example.bbyak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bbyak.databinding.ActivityRetrieveMeetingBinding

class RetrieveMeetingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRetrieveMeetingBinding

    private val meetings = ArrayList<Meeting>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRetrieveMeetingBinding.inflate(layoutInflater)

        binding.toolbar.title = "뺙 조회하기"

        binding.btAddMeeting.setOnClickListener{
            val code = binding.etMeetingName.text.toString()
            val meetingList = getMeetingList()

            if(binding.etMeetingName.text.equals("")) Toast.makeText(this, "코드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            else if (code !in meetingList) Toast.makeText(this, "잘못된 코드입니다.", Toast.LENGTH_SHORT).show()
            else {
                //TODO(미팅에 유저 추가)
                val time = ArrayList<String>()
                val mDate = getMeetingDate(code)
                for (i in mDate) {
                    time.add("0000000000000000")
                }

                val user = newUser(getUid(), getUserName(), time, false)

                usersRef.child(getUid()).child("meeting").child(code).setValue(code)
                meetingsRef.child(code).child("user").child(getUid()).setValue(user)

                getInvitedMeetingList()
                setRecyclerView()
            }
        }
        getInvitedMeetingList()
        setRecyclerView()
        setContentView(binding.root)
    }

    private fun getInvitedMeetingList(){
        //TODO(뺙 목록 가져오기)
        val meetingList = getUserMeeting()
        for (code in meetingList) {
            val meeting = getMeeting(code)
            meetings.add(meeting)
        }
    }

    private fun setRecyclerView() {
        binding.rvRetrieveMeeting.layoutManager = LinearLayoutManager(this)
        binding.rvRetrieveMeeting.adapter = RetrieveMeetingAdapter(meetings, this)
    }
}

class Meeting(
    var code: String,
    var name: String,
    var creator: String,
    var isMaster: Boolean,
    var isDone: Boolean
)
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
            if(binding.etMeetingName.text.equals("")) Toast.makeText(this, "코드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            else {
                //TODO(미팅에 유저 추가)
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
        meetings.add(Meeting("abc", "학술제 회의", "ㅁㅁㅁ", true))
        meetings.add(Meeting("abc1", "축제 회의", "ㅇㅇㅇ", false))
        meetings.add(Meeting("abc2", "캡스톤 회의", "ㅋㅋㅋ", false))
        meetings.add(Meeting("abc3", "회의", "ㄷㄷㄷ", false))
        meetings.add(Meeting("abc4", "회의", "ㄱㄱㄱ", true))
        meetings.add(Meeting("abc5", "학술제 회의", "ㅁㅁㅁ", true))
        meetings.add(Meeting("abc6", "축제 회의", "ㅇㅇㅇ", false))
        meetings.add(Meeting("abc7", "캡스톤 회의", "ㅋㅋㅋ", false))
        meetings.add(Meeting("abc8", "회의", "ㄷㄷㄷ", false))
        meetings.add(Meeting("abc9", "회의", "ㄱㄱㄱ", true))
        meetings.add(Meeting("abc10", "학술제 회의", "ㅁㅁㅁ", true))
        meetings.add(Meeting("abc11", "축제 회의", "ㅇㅇㅇ", false))
    }

    private fun setRecyclerView() {
        binding.rvRetrieveMeeting.layoutManager = LinearLayoutManager(this)
        binding.rvRetrieveMeeting.adapter = RetrieveMeetingAdapter(meetings, this)
    }
}

class Meeting(
    val code: String,
    val name: String,
    val creator: String,
    val isManager: Boolean
)
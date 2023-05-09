package com.example.bbyak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bbyak.databinding.ActivityRetrieveMeetingBinding

class RetrieveMeetingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRetrieveMeetingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRetrieveMeetingBinding.inflate(layoutInflater)

        binding.toolbar.title = "뺙 조회하기"

        setRecyclerView()
        setContentView(binding.root)
    }

    private fun setRecyclerView() {
        binding.rvRetrieveMeeting.layoutManager = LinearLayoutManager(this)
        binding.rvRetrieveMeeting.adapter = RetrieveMeetingAdapter(ArrayList(), this)
    }
}
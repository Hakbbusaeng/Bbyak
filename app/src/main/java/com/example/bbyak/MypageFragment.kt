package com.example.bbyak

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bbyak.databinding.FragmentMypageBinding

class MypageFragment : Fragment() {

    private lateinit var binding: FragmentMypageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(layoutInflater)

        binding.btManageMySchedule.setOnClickListener {
            startActivity(Intent(requireContext(), MyScheduleActivity::class.java))
        }
        binding.btCreateMeeting.setOnClickListener {
            startActivity(Intent(requireContext(), CreateMeetingActivity::class.java))
        }

        return binding.root
    }

}
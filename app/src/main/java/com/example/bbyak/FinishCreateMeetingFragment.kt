package com.example.bbyak

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bbyak.databinding.FragmentFinishCreateMeetingBinding

class FinishCreateMeetingFragment : Fragment() {

    private lateinit var binding: FragmentFinishCreateMeetingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFinishCreateMeetingBinding.inflate(layoutInflater)

        val code = requireArguments().getString("code")
        binding.tvCode.text = code;

        return binding.root
    }

}
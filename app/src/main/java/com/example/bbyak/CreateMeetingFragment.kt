package com.example.bbyak

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.bbyak.databinding.FragmentCreateMeetingBinding
import java.util.*

class CreateMeetingFragment : Fragment() {

    lateinit var binding: FragmentCreateMeetingBinding

    private val curDate by lazy {
        Calendar.getInstance().apply {
            timeZone = TimeZone.getTimeZone("Asia/Seoul")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateMeetingBinding.inflate(layoutInflater)

        Log.e(
            "curDate",
            "${curDate.get(Calendar.YEAR)}/${curDate.get(Calendar.MONTH) + 1}/${curDate.get(Calendar.DAY_OF_MONTH)}"
        )

        binding.selectCalendarView.setMinimumDate(curDate)

        return binding.root
    }
}
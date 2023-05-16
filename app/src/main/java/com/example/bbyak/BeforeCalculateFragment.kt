package com.example.bbyak

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.bbyak.databinding.FragmentBeforeCalculateBinding
import com.example.bbyak.databinding.ProfileListBinding


class BeforeCalculateFragment : Fragment() {

    private lateinit var binding: FragmentBeforeCalculateBinding
    private lateinit var profileListBinding: ProfileListBinding

    private val list by lazy {
        ArrayList<String>().apply {
            add("김ㅇㅇ"); add("이ㅇㅇ"); add("박ㅇㅇ"); add("최ㅇㅇ"); add("하ㅇㅇ"); add("한ㅇㅇ"); add("권ㅇㅇ")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBeforeCalculateBinding.inflate(layoutInflater)
        profileListBinding = ProfileListBinding.bind(binding.root)

        setList()

        profileListBinding.btViewNameList.setOnClickListener {
            startActivity(Intent(context, SubmitNameListActivity::class.java)
                .apply { putStringArrayListExtra("nameList", list) })
        }

        return binding.root
    }

    private fun setList() {
        val tvList = listOf(
            profileListBinding.profile1,
            profileListBinding.profile2,
            profileListBinding.profile3,
            profileListBinding.profile4,
            profileListBinding.profile5
        )
        if (list.size > 5) profileListBinding.tvHeadCount.text = "외 ${list.size - 5}명"
        else {
            profileListBinding.tvHeadCount.visibility = View.GONE
            profileListBinding.flProfileList.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            )
        }
        for (i in 0 until 5) {
            if (i < list.size) tvList[i].text = list[i][0].toString()
            else tvList[i].visibility = View.GONE
        }
    }

}
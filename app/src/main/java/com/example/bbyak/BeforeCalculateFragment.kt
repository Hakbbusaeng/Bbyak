package com.example.bbyak

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        profileListBinding.profile1.text = list[0][0].toString()
        profileListBinding.profile2.text = list[1][0].toString()
        profileListBinding.profile3.text = list[2][0].toString()
        profileListBinding.profile4.text = list[3][0].toString()
        profileListBinding.profile5.text = list[4][0].toString()

        profileListBinding.tvHeadCount.text = "외 ${list.size - 5}명"
    }

}
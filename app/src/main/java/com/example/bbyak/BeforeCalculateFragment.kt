package com.example.bbyak

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBeforeCalculateBinding.inflate(layoutInflater)
        profileListBinding = ProfileListBinding.bind(binding.root)

        setList()

        return binding.root
    }

    private fun setList() {
        val list = ArrayList<String>().apply {
            add("김ㅇㅇ"); add("이ㅇㅇ"); add("박ㅇㅇ"); add("최ㅇㅇ"); add("하ㅇㅇ"); add("한ㅇㅇ"); add("권ㅇㅇ")
        }
        profileListBinding.profile1.text = list[0].getFirstChar().toString()
        profileListBinding.profile2.text = list[1].getFirstChar().toString()
        profileListBinding.profile3.text = list[2].getFirstChar().toString()
        profileListBinding.profile4.text = list[3].getFirstChar().toString()
        profileListBinding.profile5.text = list[4].getFirstChar().toString()

        profileListBinding.tvHeadCount.text = "외 ${list.size - 5}명"
    }
    private fun String.getFirstChar(): Char{
        return this[0]
    }
}
package com.example.bbyak

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bbyak.databinding.FragmentBeforeCalculateBinding
import com.example.bbyak.databinding.ProfileListBinding


class BeforeCalculateFragment : Fragment() {

    private lateinit var binding: FragmentBeforeCalculateBinding

    private lateinit var list: ArrayList<String>
    
    private var meetingCode: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBeforeCalculateBinding.inflate(layoutInflater)

        meetingCode = arguments?.getString("meetingCode")
        
        getSubmitNameList()
        setRecyclerView()

        return binding.root
    }

    private fun getSubmitNameList() {
        //TODO(제출 명단 가져오기) meetingCode 사용
        list = ArrayList<String>().apply {
            add("김ㅇㅇ"); add("이ㅇㅇ"); add("박ㅇㅇ"); add("최ㅇㅇ"); add("하ㅇㅇ"); add("한ㅇㅇ"); add("권ㅇㅇ")
            add("김ㅇㅇ"); add("이ㅇㅇ"); add("박ㅇㅇ"); add("최ㅇㅇ"); add("하ㅇㅇ"); add("한ㅇㅇ"); add("권ㅇㅇ")
            add("김ㅇㅇ"); add("이ㅇㅇ"); add("박ㅇㅇ"); add("최ㅇㅇ"); add("하ㅇㅇ"); add("한ㅇㅇ"); add("권ㅇㅇ")
        }
        binding.tvHeadCount.text = "${list.size}명"
    }

    private fun setRecyclerView(){
        binding.rvNameList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNameList.adapter = NameListAdapter(list)
    }

}
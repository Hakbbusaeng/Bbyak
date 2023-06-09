package com.example.bbyak

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bbyak.databinding.FragmentBeforeCalculateBinding
import com.example.bbyak.databinding.ProfileListBinding
import kotlinx.coroutines.launch


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


        binding.tvCode.text = meetingCode
        binding.btCopy.setOnClickListener {
            val clipboard =
                requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("code", meetingCode)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "코드가 복사되었습니다", Toast.LENGTH_SHORT).show()
        }

        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            getSubmitNameList().join()
            setRecyclerView()
            binding.progressBar.visibility = View.GONE
        }

        return binding.root
    }

    private fun getSubmitNameList() =
        lifecycleScope.launch {
            val submitUserList = getSubmitUserList(meetingCode.toString())
            val submitUserNameList = ArrayList<String>()
            for (uid in submitUserList) {
                submitUserNameList.add(getUserName(uid))
            }

            list = ArrayList<String>().apply {
                for (name in submitUserNameList) {
                    add(name)
                }
            }
            binding.tvHeadCount.text = "${list.size}명"
        }

    private fun setRecyclerView() {
        binding.rvNameList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNameList.adapter = NameListAdapter(list)
    }

}
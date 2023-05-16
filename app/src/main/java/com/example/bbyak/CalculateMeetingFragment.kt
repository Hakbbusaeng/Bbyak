package com.example.bbyak

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bbyak.databinding.FragmentCalculateMeetingBinding

class CalculateMeetingFragment : Fragment() {

    private lateinit var binding: FragmentCalculateMeetingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalculateMeetingBinding.inflate(layoutInflater)

        setRecyclerView()

        return binding.root
    }

    private fun setRecyclerView(){
        binding.rvCalculateMeetingResult.layoutManager = LinearLayoutManager(context)
        binding.rvCalculateMeetingResult.adapter = CalculateMeetingResultAdapter(ArrayList(), requireContext())
    }
}
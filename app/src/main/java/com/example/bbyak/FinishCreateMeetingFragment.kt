package com.example.bbyak

import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bbyak.databinding.FragmentFinishCreateMeetingBinding


class FinishCreateMeetingFragment : Fragment() {

    private lateinit var binding: FragmentFinishCreateMeetingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFinishCreateMeetingBinding.inflate(layoutInflater)

        val code = requireArguments().getString("code")
        binding.tvCode.text = code

        binding.btCopy.setOnClickListener {
            val clipboard =
                requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("code", code)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "코드가 복사되었습니다", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

}
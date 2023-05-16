package com.example.bbyak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bbyak.databinding.ActivitySubmitNameListBinding

class SubmitNameListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubmitNameListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubmitNameListBinding.inflate(layoutInflater)

        setRecyclerView()

        setContentView(binding.root)
    }

    private fun setRecyclerView(){
        binding.rvNameList.layoutManager = LinearLayoutManager(this)
        binding.rvNameList.adapter = NameListAdapter(intent.getStringArrayListExtra("nameList")!!)
    }

}
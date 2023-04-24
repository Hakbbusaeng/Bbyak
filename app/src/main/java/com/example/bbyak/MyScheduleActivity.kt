package com.example.bbyak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bbyak.databinding.ActivityMyScheduleBinding

class MyScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "내 스케줄"

        initTableList()

        binding.rvTimeTable.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val width = binding.rvTimeTable.width
                val height = binding.rvTimeTable.height
                val itemWidth = width / 8
                val itemHeight = height / 17
                Log.e("size", "width : $itemWidth, height: $itemHeight")

                setTimeTable(itemWidth, itemHeight)
                binding.rvTimeTable.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private val tableList = ArrayList<String>()

    private fun initTableList() {
        tableList.addAll(arrayListOf("", "월", "화", "수", "목", "금", "토", "일"))
        for (i in 8..23) {
            tableList.addAll(arrayListOf(i.toString(), "", "", "", "", "", "", ""))
        }
    }

    private fun setTimeTable(width: Int, height: Int) {
        val gridLayoutManager = object : GridLayoutManager(this, 8){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.rvTimeTable.layoutManager = gridLayoutManager
        binding.rvTimeTable.adapter = TimetableAdapter(tableList, width, height)
    }


}
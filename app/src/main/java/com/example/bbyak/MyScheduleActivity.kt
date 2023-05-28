package com.example.bbyak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bbyak.databinding.ActivityMyScheduleBinding
import java.sql.Time

class MyScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyScheduleBinding
    private lateinit var adapter: TimetableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "내 스케줄"
        setSupportActionBar(binding.toolbar)

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_save -> {
                //TODO(스케줄 저장) -> returnSelectedTime() 이 함수의 매개변수로 selectedTime 값만 넣으면 됨!!
                usersRef.child(getUid()).child("schedule").setValue(getSelectedTime(adapter.getSelectedTime()))
                Toast.makeText(this, "스케줄이 저장되었습니다.", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        adapter = TimetableAdapter(tableList, width, height)
        binding.rvTimeTable.adapter = adapter
    }


}
package com.example.bbyak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.bbyak.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val FRAGMENT_HOME = 1
    private val FRAGMENT_MYPAGE = 2

    private lateinit var btHome: TextView;
    private lateinit var btMyPage: TextView;

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btHome = binding.btHome
        btMyPage = binding.btMyPage

        switchFragment(FRAGMENT_HOME)

        btHome.setOnClickListener { switchFragment(FRAGMENT_HOME) }
        btMyPage.setOnClickListener { switchFragment(FRAGMENT_MYPAGE) }

    }

    private fun switchFragment(fragment: Int){
        when(fragment) {
            FRAGMENT_HOME-> {
                binding.toolbar.title = "뺙 리스트"
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<HomeFragment>(binding.fragmentContainer.id)
                }
                btHome.setBackgroundResource(R.color.yellow)
                btMyPage.setBackgroundResource(R.color.button_grey)
            }
            FRAGMENT_MYPAGE->{
                binding.toolbar.title = "마이페이지"
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<MypageFragment>(binding.fragmentContainer.id)
                }
                btHome.setBackgroundResource(R.color.button_grey)
                btMyPage.setBackgroundResource(R.color.yellow)
            }
        }
    }
}
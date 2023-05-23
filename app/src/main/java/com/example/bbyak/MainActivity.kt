package com.example.bbyak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.bbyak.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val FRAGMENT_HOME = 1
    private val FRAGMENT_MYPAGE = 2

    private lateinit var btHome: ImageButton
    private lateinit var btMyPage: ImageButton

    private lateinit var binding: ActivityMainBinding

    private val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btHome = binding.btHome
        btMyPage = binding.btMyPage

        switchFragment(FRAGMENT_HOME)

        btHome.setOnClickListener { switchFragment(FRAGMENT_HOME) }
        btMyPage.setOnClickListener { switchFragment(FRAGMENT_MYPAGE) }

        if (user != null) {
            Log.e("로그인", "성공")
            user?.let {
                val email = it.email.toString()
                Log.e("email", email)
            }
        } else {
            Log.e("로그인", "실패")
        }
    }

    private fun switchFragment(fragment: Int){
        when(fragment) {
            FRAGMENT_HOME-> {
                binding.toolbar.title = "뺙 리스트"
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<HomeFragment>(binding.fragmentContainer.id)
                }
                btHome.setImageResource(R.drawable.ic_home_selected)
                btMyPage.setImageResource(R.drawable.ic_mypage_unselected)
            }
            FRAGMENT_MYPAGE->{
                binding.toolbar.title = "마이페이지"
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<MypageFragment>(binding.fragmentContainer.id)
                }
                btHome.setImageResource(R.drawable.ic_home_unselected)
                btMyPage.setImageResource(R.drawable.ic_mypage_selected)
            }
        }
    }
}
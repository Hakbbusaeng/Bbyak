package com.example.bbyak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.bbyak.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btLogin: Button
    private lateinit var btSignIn: TextView

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        etEmail = binding.etEmail
        etPassword = binding.etPassword
        btLogin = binding.btLogin
        btSignIn = binding.btSignIn

        btLogin.setOnClickListener { login() }
        btSignIn.setOnClickListener { goToSignIn() }

    }

    private fun goToSignIn(){
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }
    private fun login(){
        //TODO: 로그인 작업 수행
        Log.e("email", etEmail.text.toString())
        Log.e("password", etPassword.text.toString())
    }
}
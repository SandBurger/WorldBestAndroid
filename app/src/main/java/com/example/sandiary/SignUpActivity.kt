package com.example.sandiary

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.sandiary.Util.hideStatusBar
import com.example.sandiary.databinding.ActivitySignUpBinding
import com.example.sandiary.databinding.ActivitySplashBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideStatusBar()
        binding.btn.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
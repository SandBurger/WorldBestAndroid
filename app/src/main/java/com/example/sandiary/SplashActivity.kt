package com.example.sandiary

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.sandiary.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    lateinit var binding : ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 200)
    }


}
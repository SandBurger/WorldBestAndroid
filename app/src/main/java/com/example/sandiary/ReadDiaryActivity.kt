package com.example.sandiary

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.sandiary.Util.hideStatusBar
import com.example.sandiary.databinding.ActivityReadDiaryBinding
import com.example.sandiary.databinding.ActivitySplashBinding

class ReadDiaryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityReadDiaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReadDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideStatusBar()
        val diary = intent.getStringExtra("diary")
        if(diary == null){
            binding.readDateTv.visibility = View.GONE
        } else {
            binding.diaryDiaryEt.setText(diary)
        }

        binding.readExitIb.setOnClickListener {
            finish()
        }
    }
}
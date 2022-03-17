package com.example.lieterature

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.example.lieterature.databinding.ActivityMainBinding
import com.example.lieterature.viewmodel.UIViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var mUIViewModel: UIViewModel
    private var isDarkMode = true
    var isSwitched = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        anim()
        hideSystemUI()
        initViewModel()
        initSendButton()
        initSpinner()
        initView()
        initTimer()

    }

    private fun initViewModel(){
        mUIViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(UIViewModel::class.java)
        mUIViewModel.count.observe(this, Observer{
            binding.mainTest.text = it.toString()
        })
        mUIViewModel.textSize.observe(this, Observer {
            binding.mainTest.textSize = it.toFloat()
        })
        mUIViewModel.time.observe(this, Observer {
            binding.mainTimer.text = it.toString()
        })
    }
    fun anim(){
        binding.mainLottie.setOnClickListener {
            if (isSwitched) {
                binding.mainLottie.setMinAndMaxProgress(0.5f, 1.0f)
                binding.mainLottie.playAnimation()
                isSwitched = false
            } else {
                binding.mainLottie.setMinAndMaxProgress(0.0f, 0.5f)
                binding.mainLottie.playAnimation()
                isSwitched = true
            }
        }
    }
    fun initSendButton(){
        binding.mainBtn.setOnClickListener {
            mUIViewModel.getText(binding.mainInput.text.toString())
            Toast.makeText(this, "값이 변경되었습니다.",Toast.LENGTH_SHORT).show()
        }
    }
    private fun initSpinner(){
        var textSize = arrayOf(12, 14, 16, 18)
        binding.mainSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, textSize)
        binding.mainSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                mUIViewModel.changeTextSize(textSize[position])
            }
        }
    }
    private fun initView(){
        var time = 0
        binding.mainBtn.setOnClickListener {
            mUIViewModel.checkTime(time)
            time++
        }
    }
    private fun initTimer(){

    }
    private fun hideSystemUI() {
       supportActionBar?.hide()
    }


}
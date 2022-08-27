package com.example.sandiary

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sandiary.Util.hideStatusBar
import com.example.sandiary.databinding.ActivityDiaryBinding
import com.example.sandiary.databinding.ActivitySplashBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DiaryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDiaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideStatusBar()
        val localDate = LocalDate.now()
        val date = localDate.format(DateTimeFormatter.ofPattern("M월 d일"))
        binding.diaryDateTv.text = date

        val diary = intent.getStringExtra("diary")
        if(diary == null){
            binding.diaryTimeStampTv.visibility = View.GONE
            binding.diaryDiaryEt.addTextChangedListener(object : TextWatcher {
                var text = binding.diaryDiaryEt.text.toString()
                override fun afterTextChanged(p0: Editable?) {
                    //처음이랑 일치할 때, 처음과 다를 때,
                    if(text == ""){
                        when(binding.diaryDiaryEt.text.toString()){
                            text -> {
                                binding.diarySaveTv.setTextColor(getColor(R.color.line_grey))
                                binding.diarySaveTv.isClickable = false
                            }
                            else -> {
                                binding.diarySaveTv.setTextColor(getColor(R.color.line_black))
                                binding.diarySaveTv.setOnClickListener {
                                    finish()
                                }
                                binding.diarySaveTv.isClickable = true
                            }
                        }
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            })
        } else {
            binding.diaryDiaryEt.setText(diary)
            binding.diaryDiaryEt.isEnabled = false
            binding.diarySaveTv.visibility = View.GONE
            binding.diaryRemoveIb.visibility = View.VISIBLE
        }

        binding.diaryExitIb.setOnClickListener {
            showDialog()
        }

    }


    private fun showDialog(){
        val layoutInflater = LayoutInflater.from(this)
        val view = layoutInflater.inflate(R.layout.dialog_exit, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(view)
            .create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        view.findViewById<TextView>(R.id.dialog_exit_positive_button_tv).setOnClickListener {
            alertDialog.dismiss()
            finish()
        }
        view.findViewById<TextView>(R.id.dialog_exit_negative_button_tv).setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

}
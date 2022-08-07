package com.example.sandiary

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sandiary.Util.hideStatusBar
import com.example.sandiary.Util.setWindowFlag
import com.example.sandiary.databinding.ActivitySeeAllBinding
import com.example.sandiary.databinding.TestBinding
import com.example.sandiary.ui.SeeAllRVAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class SeeAllActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeeAllBinding
    private var selectedDay : LocalDate? = null
    private lateinit var pickerBinding : TestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var dummyDiaryList = ArrayList<Diary>()
        dummyDiaryList.add(Diary(0, "CODE' -> CODE \n" +
                "RETURN -> return RHS semi\n" +
                "CDECL -> class id lbrace ODECL rbrace\n" +
                "ODECL -> VDECL ODECL\n" +
                "ODECL -> FDECL ODECL\n" +
                "ODECL -> ''"))
        dummyDiaryList.add(Diary(1, "test1"))
        pickerBinding = TestBinding.inflate(layoutInflater)
        binding = ActivitySeeAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.seeAllRv.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val diaryRVAdapter = SeeAllRVAdapter(dummyDiaryList)
        diaryRVAdapter.itemClickListener(object : SeeAllRVAdapter.ItemClickListener{
            override fun onClick(diary: Diary) {
                showDialog()
            }
        })
        binding.seeAllRv.adapter = diaryRVAdapter


        binding.seeAllExitIb.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
        val view2 = layoutInflater.inflate(R.layout.dialog_month_picker, null)
        val dialog2 = AlertDialog.Builder(this).setView(view2).create()
        binding.seeAllCalendarDialogIb.setOnClickListener {
            dialog2.show()
            //TestDialog().show(supportFragmentManager, "Test")
        }
        initView()
        hideStatusBar()
    }

    private fun initView(){
        val localDate = LocalDate.now()
        val date = localDate.format(DateTimeFormatter.ofPattern("YYYY.MM.dd"))
        binding.seeAllDateTv.text = date
    }

    private fun showDialog(){
        val layoutInflater = LayoutInflater.from(this)
        val view = layoutInflater.inflate(R.layout.dialog_exit, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(view)
            .create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        view.findViewById<TextView>(R.id.dialog_exit_title_tv).setText(R.string.dialog_see_title)
        view.findViewById<TextView>(R.id.dialog_exit_message_tv).setText(R.string.dialog_see_message)
        view.findViewById<TextView>(R.id.dialog_exit_positive_button_tv).setOnClickListener {
            alertDialog.dismiss()
        }
        view.findViewById<TextView>(R.id.dialog_exit_negative_button_tv).setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}
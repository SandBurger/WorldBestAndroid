package com.example.sandiary

import android.app.ProgressDialog.show
import android.content.ContentProvider
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sandiary.Util.setWindowFlag
import com.example.sandiary.databinding.ActivitySeeAllBinding
import com.example.sandiary.databinding.DialogDatePickerBinding
import com.example.sandiary.databinding.ItemCalendarDayBinding
import com.example.sandiary.ui.SeeAllRVAdapter
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class SeeAllActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeeAllBinding
    private var selectedDay : LocalDate? = null
    private lateinit var pickerBinding : DialogDatePickerBinding
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
        pickerBinding = DialogDatePickerBinding.inflate(layoutInflater)
        binding = ActivitySeeAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val layoutInflater = LayoutInflater.from(this)
        val view = layoutInflater.inflate(R.layout.dialog_see_all, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(view)
            .create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.seeAllRv.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val diaryRVAdapter = SeeAllRVAdapter(dummyDiaryList)
        diaryRVAdapter.itemClickListener(object : SeeAllRVAdapter.ItemClickListener{
            override fun onClick(diary: Diary) {
                alertDialog.show()
                alertDialog.findViewById<TextView>(R.id.dialog_see_all_cancel_tv)!!.setOnClickListener {
                    alertDialog.hide()
                }
                alertDialog.findViewById<TextView>(R.id.dialog_see_all_remove_tv)!!.setOnClickListener {
                    alertDialog.hide()
                }
            }
        })
        binding.seeAllRv.adapter = diaryRVAdapter


        binding.seeAllExitIb.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
        val view2 = layoutInflater.inflate(R.layout.dialog_date_picker, null)
        val dialog2 = AlertDialog.Builder(this).setView(view2).create()
        binding.seeAllCalendarDialogIb.setOnClickListener {
            dialog2.show()
            //TestDialog().show(supportFragmentManager, "Test")
        }
        initView()
        statusBar()
    }

    private fun statusBar(){
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun initView(){
        val localDate = LocalDate.now()
        val date = localDate.format(DateTimeFormatter.ofPattern("YYYY.MM.dd"))
        binding.seeAllDateTv.text = date

    }
}
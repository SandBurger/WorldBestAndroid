package com.example.sandiary

import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sandiary.Util.setWindowFlag
import com.example.sandiary.databinding.ActivitySeeAllBinding
import com.example.sandiary.ui.SeeAllRVAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SeeAllActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeeAllBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var dummyDiaryList = ArrayList<Diary>()
        dummyDiaryList.add(Diary(0, "CODE' -> CODE \n" +
                "CODE -> VDECL CODE\n" +
                "CODE -> FDECL CODE\n" +
                "CODE -> CDECL CODE\n" +
                "CODE -> ''\n" +
                "VDECL -> vtype id semi\n" +
                "VDECL -> vtype id ASSIGN semi\n" +
                "ASSIGN -> id assign RHS\n" +
                "RHS -> EXPR\n" +
                "RHS -> literal\n" +
                "RHS -> character\n" +
                "RHS -> boolstr\n" +
                "EXPR -> EXPR addsub EXPR \n" +
                "EXPR -> EXPR multidiv EXPR\n" +
                "EXPR -> lparen EXPR rparen\n" +
                "EXPR -> id\n" +
                "EXPR -> num\n" +
                "FDECL -> vtype id lparen ARG rparen lbrace BLOCK RETURN rbrace\n" +
                "ARG -> vtype id MOREARGS\n" +
                "ARG -> ''\n" +
                "MOREARGS -> comma vtype id MOREARGS\n" +
                "MOREARGS -> ''\n" +
                "BLOCK -> STMT BLOCK\n" +
                "BLOCK -> ''\n" +
                "STMT -> VDECL\n" +
                "STMT -> ASSIGN semi\n" +
                "STMT -> if lparen COND rparen lbrace BLOCK rbrace ELSE\n" +
                "STMT -> while lparen COND rparen lbrace BLOCK rbrace\n" +
                "COND -> COND comp COND\n" +
                "COND -> boolstr\n" +
                "ELSE -> else lbrace BLOCK rbrace\n" +
                "ELSE -> ''\n" +
                "RETURN -> return RHS semi\n" +
                "CDECL -> class id lbrace ODECL rbrace\n" +
                "ODECL -> VDECL ODECL\n" +
                "ODECL -> FDECL ODECL\n" +
                "ODECL -> ''"))
        dummyDiaryList.add(Diary(1, "test1"))

        binding = ActivitySeeAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val layoutInflater = LayoutInflater.from(this)
        val view = layoutInflater.inflate(R.layout.dialog_see_all, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(view)
            .create()

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
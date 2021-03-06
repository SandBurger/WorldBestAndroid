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
import com.example.sandiary.Util.setWindowFlag
import com.example.sandiary.databinding.ActivityWriteBinding

class WriteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        statusBar()
        binding.writeExitIb.setOnClickListener {
            showDialog()
        }

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

    private fun showDialog(){
        val layoutInflater = LayoutInflater.from(this)
        val view = layoutInflater.inflate(R.layout.dialog_exit, null)

        view.findViewById<TextView>(R.id.dialog_exit_positive_button_tv).setText(R.string.exit)
        view.findViewById<TextView>(R.id.dialog_exit_title_tv).setText(R.string.dialog_write_title)
        view.findViewById<TextView>(R.id.dialog_exit_message_tv).setText(R.string.dialog_write_message)
        val alertDialog = AlertDialog.Builder(this)
            .setView(view)
            .create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        view.findViewById<TextView>(R.id.dialog_exit_positive_button_tv).setOnClickListener {
            alertDialog.dismiss()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<TextView>(R.id.dialog_exit_negative_button_tv).setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }


}
package com.example.sandiary

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.example.sandiary.Util.setWindowFlag
import com.example.sandiary.databinding.ActivityMainBinding
import com.example.sandiary.ui.calendar.CalendarFragment
import com.example.sandiary.ui.home.HomeFragment
import com.example.sandiary.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private var runnable : Runnable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBar()
        binding.navView.setOnNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.navigation_calendar -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, CalendarFragment()).commit()
                return true
            }
            R.id.navigation_home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, HomeFragment()).commit()
                return true

            }
            R.id.navigation_settings -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingsFragment()).commit()
                return true
            }
        }
        return false
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



}

package com.example.sandiary

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.sandiary.Util.hideStatusBar
import com.example.sandiary.Util.setWindowFlag
import com.example.sandiary.databinding.ActivityMainBinding
import com.example.sandiary.ui.calendar.CalendarFragment
import com.example.sandiary.ui.home.HomeFragment
import com.example.sandiary.ui.settings.SettingsFragment
import com.example.sandiary.ui.settings.SettingsViewModel
import com.google.android.material.navigation.NavigationBarView
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var runnable : Runnable? = null
    private val settingsViewModel : SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        settingsViewModel.text.observe(this, Observer {

        })
        setContentView(binding.root)
        hideStatusBar()
        initNavigation()
    }

    private fun initNavigation(){
        binding.navView.run {
            setOnItemSelectedListener { item ->
                when(item.itemId){
                    R.id.navigation_calendar -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, CalendarFragment()).commit()
                    }
                    R.id.navigation_home -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, HomeFragment()).commit()
                    }
                    R.id.navigation_settings -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, SettingsFragment()).commit()
                    }
                }
                true
            }
            selectedItemId = R.id.navigation_home
        }
    }
}

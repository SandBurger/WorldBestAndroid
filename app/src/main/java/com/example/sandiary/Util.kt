package com.example.sandiary

import android.app.Activity
import android.os.Build
import android.view.WindowManager
import androidx.core.view.WindowCompat

class Util {
    fun Activity.setStatusBarTransparent() {
        window.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        if(Build.VERSION.SDK_INT >= 30) {	// API 30 에 적용
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }
}
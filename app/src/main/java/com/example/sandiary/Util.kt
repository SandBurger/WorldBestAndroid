package com.example.sandiary

import android.app.Activity
import android.os.Build
import android.view.WindowManager
import androidx.core.view.WindowCompat

object Util{
    fun Activity.setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

}
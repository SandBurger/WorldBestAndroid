package com.example.sandiary.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class SettingsViewModel : ViewModel() {

    private val mutableText = MutableLiveData<String?>()
    val text : LiveData<String?> get() = mutableText
    var settings = "settings3"
    fun currentFragment(text: String?){
        mutableText.value = text
        if(text == "settings"){
            settings = text
        } else {
            settings = "s"
        }
    }
}
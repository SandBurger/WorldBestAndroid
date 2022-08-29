package com.example.sandiary.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class SettingsViewModel : ViewModel() {

    private val mutableText = MutableLiveData<String?>()
    val text : LiveData<String?> get() = mutableText

    fun currentFragment(text: String?){
        mutableText.value = text
    }
}
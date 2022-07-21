package com.example.sandiary.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        val localDate = LocalDate.now()
        val date = localDate.format(DateTimeFormatter.ofPattern("M월 dd일"))
        value = date
    }
    val text: LiveData<String> = _text
}
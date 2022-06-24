package com.example.sandiary.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddScheduleViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        val localDate = LocalDate.now()
        val date = localDate.format(DateTimeFormatter.ofPattern("YYYY년 MM월 dd일"))
        value = date
    }
    private val _date = MutableLiveData<String>().apply {

    }
    val text: LiveData<String> = _text
    val date: LiveData<String> = _date
}
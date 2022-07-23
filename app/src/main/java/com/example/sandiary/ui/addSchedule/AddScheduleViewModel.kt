package com.example.sandiary.ui.addSchedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddScheduleViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
//        val localDate = LocalDate.now()
//        val date = localDate.format(DateTimeFormatter.ofPattern("YYYY.MM.dd"))
//        value = date
    }

    val text: LiveData<String> = _text
}
package com.example.sandiary.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import com.kizitonwose.calendarview.model.DayOwner
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        //val localDate = DayOwner.THIS_MONTH
        //Log.d("dd","${localDate}")
        //val date = localDate.format(DateTimeFormatter.ofPattern("YYYY년 MM월"))
        //value = localDate.toString()
    }
    val text: LiveData<String> = _text
}
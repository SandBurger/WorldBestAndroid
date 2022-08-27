package com.example.sandiary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class SeeAllViewModel : ViewModel() {
    private val mutableSelectedDate = MutableLiveData<LocalDate?>()
    val selectedDate : LiveData<LocalDate?> get() = mutableSelectedDate

    fun selectDate(localDate: LocalDate?){
        mutableSelectedDate.value = localDate
    }
}
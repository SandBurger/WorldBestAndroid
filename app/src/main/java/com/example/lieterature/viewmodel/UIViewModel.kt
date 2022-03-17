package com.example.lieterature.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.SystemClock.sleep
import android.view.View
import androidx.lifecycle.AndroidViewModel

import androidx.lifecycle.MutableLiveData

import com.example.lieterature.model.DataStoreModule

class UIViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var dataStoreModule : DataStoreModule
    var count = MutableLiveData<String>()
    var textSize = MutableLiveData<Int>()
    var time = MutableLiveData<Int>()
    var name = ""
    init{
        count.value = ""
        textSize.value = 0
        time.value = 0
    }

    fun getText(text : String){
        count.value = text
    }
    fun changeTextSize(size : Int){
        textSize.value = size
    }
    fun checkTime(timer : Int){
        time.value = timer
    }

}

private operator fun <T> MutableLiveData<T>.plusAssign(t: T) {

}

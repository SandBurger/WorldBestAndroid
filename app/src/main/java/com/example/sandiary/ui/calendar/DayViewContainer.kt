package com.example.sandiary.ui.calendar

import android.view.View
import com.example.sandiary.databinding.ItemCalendarDayBinding
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view : View) : ViewContainer(view) {
    val textView = ItemCalendarDayBinding.bind(view).itemCalendarDayTv

}
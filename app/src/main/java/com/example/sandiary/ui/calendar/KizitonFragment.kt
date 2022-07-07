package com.example.sandiary.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sandiary.databinding.FragmentCalendarBinding
import com.example.sandiary.databinding.FragmentKizitonBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

class KizitonFragment : Fragment() {
    private var _binding: FragmentKizitonBinding? = null
    //
//    // This property is only valid between onCreateView and
//    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //binding = FragmentCalendarBinding.inflate(inflater, container, false)
        //    ViewModelProvider(this).get(CalendarViewModel::class.java)
        _binding = FragmentKizitonBinding.inflate(inflater, container, false)


        return binding.root
    }
}
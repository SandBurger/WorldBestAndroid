package com.example.sandiary.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sandiary.MainActivity
import com.example.sandiary.R
import com.example.sandiary.databinding.FragmentCalendarBinding
import com.example.sandiary.ui.schedule.AddScheduleFragment
import android.util.Log

class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel
    //lateinit var binding : FragmentCalendarBinding
    private var _binding: FragmentCalendarBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("das1","dasda")
        //binding = FragmentCalendarBinding.inflate(inflater, container, false)
        calendarViewModel =
            ViewModelProvider(this).get(CalendarViewModel::class.java)
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.calendarFloatingBtn.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, AddScheduleFragment()).commit()
        }
        binding.calendarView.isShowDaysOfWeekTitle = false
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("das","das",)
    }
}
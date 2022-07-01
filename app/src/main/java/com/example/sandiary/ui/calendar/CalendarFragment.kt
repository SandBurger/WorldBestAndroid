package com.example.sandiary.ui.calendar

import android.graphics.Color
import android.icu.util.Calendar
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
        //binding = FragmentCalendarBinding.inflate(inflater, container, false)
        calendarViewModel =
            ViewModelProvider(this).get(CalendarViewModel::class.java)
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val localDate = LocalDate.now()
        val date = localDate.format(DateTimeFormatter.ofPattern("YYYY.MM"))
        binding.seeAllDateTv.text = date

        binding.calendarCalendarCv.setOnMonthChangeListener {
            Log.d("month", "${it.monthName}")
            val date = it.monthName.split(' ')
            val text = "${date[1]}.${getMonth(date[0])}"
            binding.seeAllDateTv.setText(text)
        }
        binding.calendarFloatingBtn.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, AddScheduleFragment()).commit()
        }

        return binding.root
    }

    private fun getMonth(string : String) : String{
        val month =
            when(string) {
                "January" -> "01"
                "February" -> "02"
                "March" -> "03"
                "April" -> "04"
                "May" -> "05"
                "June" -> "06"
                "July" -> "07"
                "August" -> "08"
                "September" -> "09"
                "October" -> "10"
                "November" -> "11"
                else -> "12"
            }
        return month
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
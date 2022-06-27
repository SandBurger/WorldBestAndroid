package com.example.sandiary.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.applikeysolutions.cosmocalendar.view.CalendarView
import com.example.sandiary.MainActivity
import com.example.sandiary.R
import com.example.sandiary.databinding.FragmentAddScheduleBinding
import com.example.sandiary.ui.calendar.CalendarFragment
import java.text.DateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class AddScheduleFragment : Fragment() {
    private lateinit var addScheduleViewModel: AddScheduleViewModel
    private var _binding: FragmentAddScheduleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        addScheduleViewModel =
            ViewModelProvider(this).get(AddScheduleViewModel::class.java)
        _binding = FragmentAddScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addScheduleExitIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, CalendarFragment()).commit()
        }
        val dateTv = binding.addScheduleDateTv
        addScheduleViewModel.text.observe(viewLifecycleOwner, Observer {
            dateTv.text = it
        })
        binding.addScheduleStartTimeTv.setOnClickListener {
            changeTimePicker(binding.addScheduleStartTimePickerTp)
        }
        binding.addScheduleEndTimeTv.setOnClickListener {
            changeTimePicker(binding.addScheduleEndTimePickerTp)
        }

        binding.addScheduleStartDayTv.setOnClickListener {
            if(binding.addScheduleStartCalendarCv.selectedDays.isNotEmpty()) {
                val date = binding.addScheduleStartCalendarCv.selectedDays.get(0).calendar.time.toString()
                val dateFormat = date.split(' ')
                val dayOfWeek = getDayOfWeek(dateFormat[0])
                val month = getMonth(dateFormat[1])
                val day = dateFormat[2]
                val year = dateFormat[5]
                Log.d("test2", date)
                binding.addScheduleStartDayTv.text = "${year}.${month}.${day}(${dayOfWeek})"
            }
            ChangeCalendar(binding.addScheduleStartCalendarCv)
        }


        binding.addScheduleEndDayTv.setOnClickListener {
            if(binding.addScheduleEndCalendarCv.selectedDays.isNotEmpty()) {
                val date = binding.addScheduleEndCalendarCv.selectedDays.get(0).calendar.time.toString()
                val dateFormat = date.split(' ')
                val dayOfWeek = getDayOfWeek(dateFormat[0])
                val month = getMonth(dateFormat[1])
                val day = dateFormat[2]
                val year = dateFormat[5]
                Log.d("test2", date)
                binding.addScheduleEndDayTv.text = "${year}.${month}.${day}(${dayOfWeek})"
            }
            ChangeCalendar(binding.addScheduleEndCalendarCv)
        }

        return root
    }
    private fun ChangeCalendar(calendarView: CalendarView){
        if(calendarView == binding.addScheduleStartCalendarCv){
            if(calendarView.visibility == View.VISIBLE){
                calendarView.visibility = View.GONE
            }
            else{
                calendarView.visibility = View.VISIBLE
                binding.addScheduleEndCalendarCv.visibility = View.GONE
            }
        }
        else {
            if(calendarView.visibility == View.VISIBLE){
                calendarView.visibility = View.GONE
            }
            else{
                calendarView.visibility = View.VISIBLE
                binding.addScheduleStartCalendarCv.visibility = View.GONE
            }
        }
    }

    private fun changeTimePicker(timePicker: TimePicker) {
        if(timePicker == binding.addScheduleStartTimePickerTp){
            if(timePicker.visibility == View.VISIBLE){
                timePicker.visibility = View.GONE
            }
            else{
                timePicker.visibility = View.VISIBLE
                binding.addScheduleEndTimePickerTp.visibility = View.GONE
            }
        }
        else {
            if(timePicker.visibility == View.VISIBLE){
                timePicker.visibility = View.GONE
            }
            else{
                timePicker.visibility = View.VISIBLE
                binding.addScheduleStartTimePickerTp.visibility = View.GONE
            }
        }
    }

    private fun getDayOfWeek(string: String) : String{
        val dayOfWeek =
            when(string) {
                "Mon" -> "월"
                "Tue" -> "화"
                "Wed" -> "수"
                "Thu" -> "목"
                "Fri" -> "금"
                "Sat" -> "토"
                else -> "일"
            }
        return dayOfWeek
    }
    private fun getMonth(string : String) : String{
        val month =
            when(string) {
                "Jan" -> "01"
                "Feb" -> "02"
                "Mar" -> "03"
                "Apr" -> "04"
                "May" -> "05"
                "Jun" -> "06"
                "Jul" -> "07"
                "Aug" -> "08"
                "Sep" -> "09"
                "Oct" -> "10"
                "Nov" -> "11"
                else -> "12"
            }
        return month
    }
}
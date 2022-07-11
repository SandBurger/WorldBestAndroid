package com.example.sandiary.ui.schedule

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.applikeysolutions.cosmocalendar.view.CalendarView
import com.example.sandiary.MainActivity
import com.example.sandiary.Plan
import com.example.sandiary.R
import com.example.sandiary.databinding.FragmentAddScheduleBinding
import com.example.sandiary.function.PlanDatabase
import com.example.sandiary.ui.calendar.CalendarFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class AddScheduleFragment : Fragment() {
    private lateinit var addScheduleViewModel: AddScheduleViewModel
    private var _binding: FragmentAddScheduleBinding? = null
    private var planDB : PlanDatabase? = null
    var startDay : String = ""
    var endDay : String = ""
    var startTime : String = ""
    var endTime : String = ""
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        addScheduleViewModel =
            ViewModelProvider(this).get(AddScheduleViewModel::class.java)
        _binding = FragmentAddScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initFragment()

        binding.addScheduleExitIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, CalendarFragment()).commit()
        }
        val dateTv = binding.addScheduleDateTv
        addScheduleViewModel.text.observe(viewLifecycleOwner, Observer {
            dateTv.text = it
        })

        binding.addScheduleSaveTv.setOnClickListener {
           val plan = Plan(binding.addScheduleWriteDiaryEt.text.toString(),7, 13, 13 ,endTime, "22", "end")
            CoroutineScope(Dispatchers.IO).launch {
                planDB!!.planDao().insertPlan(plan)
                Log.d("insertData","dd")
            }
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, CalendarFragment()).commit()
        }

        binding.addScheduleStartTimeTv.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                changeTimePicker(binding.addScheduleStartTimePickerTp, binding.addScheduleStartTimeTv)
            }
        }
        binding.addScheduleEndTimeTv.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                changeTimePicker(binding.addScheduleEndTimePickerTp, binding.addScheduleEndTimeTv)
            }
        }
        binding.addScheduleStartDayTv.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                changeCalendar(binding.addScheduleStartCalendarCv, binding.addScheduleStartDayTv)
            }
        }
        binding.addScheduleEndDayTv.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                changeCalendar(binding.addScheduleEndCalendarCv, binding.addScheduleEndDayTv)
            }
        }
        binding.addScheduleAlarmTv.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                numberPickerVisibility()
            }
        }
        return root
    }
    private fun initFragment(){
        planDB = PlanDatabase.getInstance(requireContext())
        binding.addScheduleAlarmNp.minValue = 0
        binding.addScheduleAlarmNp.maxValue = 60
    }

    private fun getDate(calendarView: CalendarView, textView: TextView){
        if(calendarView.selectedDays.isNotEmpty()){
            val date = calendarView.selectedDays.get(0).calendar.time.toString()
            val dateFormat = date.split(' ')
            val dayOfWeek = getDayOfWeek(dateFormat[0])
            val month = getMonth(dateFormat[1])
            val day = dateFormat[2]
            val year = dateFormat[5]
            textView.text = "${year}.${month}.${day}(${dayOfWeek})"
            when(textView){
                binding.addScheduleEndDayTv -> endDay = "${year}.${month}.${day}"
                else -> startDay = "${year}.${month}.${day}"
            }
        }
    }

    private fun getTime(timePicker: TimePicker, textView: TextView){
        var hour = timePicker.hour
        val minute = timePicker.minute
        var range = "오전"
        var hourString = ""
        var minuteString = ""
        if(hour < 10)  {
            hourString = "0${hour}"
        } else {
            if(hour > 12){
                hour -= 12
                hourString = "0${hour}"
                range = "오후"
            } else {
                hourString = "${hour}"
            }
        }
        if(minute < 10) {
            minuteString = "0${minute}"
        } else {
            minuteString = "${minute}"
        }
        textView.text = "${range} ${hourString}:${minuteString}"
        when(textView){
            binding.addScheduleEndTimeTv -> endTime = "${hourString}.${minuteString}"
            else -> startTime = "${hourString}.${minuteString}"
        }
    }

    private fun changeCalendar(calendarView: CalendarView, textView:TextView){
        changeText()
        binding.addScheduleStartTimePickerTp.visibility = View.GONE
        binding.addScheduleEndTimePickerTp.visibility = View.GONE
        binding.addScheduleAlarmNp.visibility = View.GONE
        binding.addScheduleAfterTv.visibility = View.GONE

        if(calendarView == binding.addScheduleStartCalendarCv){
            if(calendarView.visibility == View.VISIBLE){
                calendarView.visibility = View.GONE
            } else{
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.active))
                calendarView.visibility = View.VISIBLE
                binding.addScheduleEndCalendarCv.visibility = View.GONE
            }
        }
        else {
            if(calendarView.visibility == View.VISIBLE){
                calendarView.visibility = View.GONE
            } else{
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.active))
                calendarView.visibility = View.VISIBLE
                binding.addScheduleStartCalendarCv.visibility = View.GONE
            }
        }
    }

    private fun changeTimePicker(timePicker: TimePicker, textView: TextView) {
        changeText()
        binding.addScheduleStartCalendarCv.visibility = View.GONE
        binding.addScheduleEndCalendarCv.visibility = View.GONE
        binding.addScheduleAlarmNp.visibility = View.GONE
        binding.addScheduleAfterTv.visibility = View.GONE

        if(timePicker == binding.addScheduleStartTimePickerTp){
            if(timePicker.visibility == View.VISIBLE){
                timePicker.visibility = View.GONE
            } else{
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.active))
                timePicker.visibility = View.VISIBLE
                binding.addScheduleEndTimePickerTp.visibility = View.GONE
            }
        }
        else {
            if(timePicker.visibility == View.VISIBLE){
                timePicker.visibility = View.GONE
            } else{
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.active))
                timePicker.visibility = View.VISIBLE
                binding.addScheduleStartTimePickerTp.visibility = View.GONE
            }
        }
    }

    private fun numberPickerVisibility(){
        changeText()
        binding.addScheduleStartCalendarCv.visibility = View.GONE
        binding.addScheduleEndCalendarCv.visibility = View.GONE
        binding.addScheduleAlarmNp.visibility = View.GONE
        binding.addScheduleAfterTv.visibility = View.GONE

        val numberPicker = binding.addScheduleAlarmNp
        if(numberPicker.visibility == View.VISIBLE){
            numberPicker.visibility = View.GONE
            binding.addScheduleAfterTv.visibility = View.GONE
        } else{

            numberPicker.visibility = View.VISIBLE
            binding.addScheduleAfterTv.visibility = View.VISIBLE
        }
    }

    private fun changeText() {
        if (binding.addScheduleStartCalendarCv.visibility == View.VISIBLE) {
            getDate(binding.addScheduleStartCalendarCv, binding.addScheduleStartDayTv)
            binding.addScheduleStartDayTv.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.line_black
                )
            )
        } else if (binding.addScheduleEndCalendarCv.visibility == View.VISIBLE) {
            getDate(binding.addScheduleEndCalendarCv, binding.addScheduleEndDayTv)
            binding.addScheduleEndDayTv.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.line_black
                )
            )
        }
        if (binding.addScheduleStartTimePickerTp.visibility == View.VISIBLE) {
            getTime(binding.addScheduleStartTimePickerTp, binding.addScheduleStartTimeTv)
            binding.addScheduleStartTimeTv.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.line_black
                )
            )
        } else if (binding.addScheduleEndTimePickerTp.visibility == View.VISIBLE) {
            getTime(binding.addScheduleEndTimePickerTp, binding.addScheduleEndTimeTv)
            binding.addScheduleEndTimeTv.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.line_black
                )
            )
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
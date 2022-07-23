package com.example.sandiary.ui.addSchedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.TextView
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sandiary.MainActivity
import com.example.sandiary.Plan
import com.example.sandiary.R
import com.example.sandiary.databinding.FragmentAddScheduleBinding
import com.example.sandiary.databinding.ItemCalendarDayBinding
import com.example.sandiary.function.PlanDatabase
import com.example.sandiary.ui.calendar.CalendarFragment
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

class AddScheduleFragment : Fragment() {
    private lateinit var addScheduleViewModel: AddScheduleViewModel
    private var _binding: FragmentAddScheduleBinding? = null
    private var planDB : PlanDatabase? = null
    var startDay : String = ""
    var endDay : String = ""
    var startTime : String = ""
    var endTime : String = ""
    var flag : Int = 0
    var selectedStartDay : LocalDate? = null
    var selectedEndDay : LocalDate? = null
    val daysOfWeek = arrayOf(
        DayOfWeek.SUNDAY,
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY
    )
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        addScheduleViewModel =
            ViewModelProvider(this).get(AddScheduleViewModel::class.java)
        _binding = FragmentAddScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initFragment()
        Log.d("alarm", "${binding.addScheduleAlarmNp.value}")

        binding.addScheduleExitIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, CalendarFragment()).commit()
        }
        val dateTv = binding.addScheduleStartTv
        addScheduleViewModel.text.observe(viewLifecycleOwner, Observer {
            dateTv.text = it
        })
        class DayViewContainer(view : View) : ViewContainer(view) {
            val textView = ItemCalendarDayBinding.bind(view).itemCalendarDayTv
            val imageView = ItemCalendarDayBinding.bind(view).itemCalendarDayIv
            lateinit var day : CalendarDay
            init {
                view.setOnClickListener{
                    if (day.owner == DayOwner.THIS_MONTH){
                        if(binding.addScheduleStartCalendarCv.visibility == View.VISIBLE){
                            val currentSelection = selectedStartDay
                            if(currentSelection == day.date){
                                selectedStartDay = null
                                binding.addScheduleStartCalendarCv.notifyDateChanged(currentSelection)
                            } else {
                                selectedStartDay = day.date
                                binding.addScheduleStartDayTv.text = getDate(day.date)
                                binding.addScheduleStartCalendarCv.notifyDateChanged(day.date)
                                if (currentSelection != null){
                                    binding.addScheduleStartCalendarCv.notifyDateChanged(currentSelection)
                                }
                            }
                        }
                        else{
                            val currentSelection = selectedEndDay
                            if(currentSelection == day.date){
                                selectedEndDay = null
                                binding.addScheduleEndCalendarCv.notifyDateChanged(currentSelection)
                            } else {
                                selectedEndDay = day.date
                                binding.addScheduleEndDayTv.text = getDate(day.date)
                                binding.addScheduleEndCalendarCv.notifyDateChanged(day.date)
                                if (currentSelection != null){
                                    binding.addScheduleEndCalendarCv.notifyDateChanged(currentSelection)
                                }
                            }
                        }
                    }
                }
            }
        }
        CoroutineScope(Dispatchers.Main).launch{
            binding.addScheduleStartCalendarCv.dayBinder = object : DayBinder<DayViewContainer> {
                override fun create(view: View) =  DayViewContainer(view)
                override fun bind(container: DayViewContainer, day: CalendarDay) {
                    container.day = day
                    container.textView.text = day.date.dayOfMonth.toString()
                    if(day.owner == DayOwner.THIS_MONTH){
                        when{
                            day.date == selectedStartDay -> {
                                container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                                container.imageView.visibility = View.VISIBLE
                                Log.d("date","${day.date}")
                                Log.d("date","${day.date.dayOfMonth}")
                                Log.d("date","${day.date.month}")
                            }
                            else -> {
                                container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))
                                container.imageView.visibility = View.INVISIBLE
                            }
                        }
                    } else {
                        container.textView.setTextColor(ContextCompat
                            .getColor(requireContext(),R.color.line_grey))
                    }
                }
            }

            val currentMonth = YearMonth.now()
            val firstMonth = currentMonth.minusMonths(10)
            val lastMonth = currentMonth.plusMonths(10)
            binding.addScheduleStartCalendarCv.setup(firstMonth, lastMonth, daysOfWeek.first())
            binding.addScheduleStartCalendarCv.scrollToMonth(currentMonth)
        }
        CoroutineScope(Dispatchers.Main).launch{
            binding.addScheduleEndCalendarCv.dayBinder = object : DayBinder<DayViewContainer> {
                override fun create(view: View) =  DayViewContainer(view)
                override fun bind(container: DayViewContainer, day: CalendarDay) {
                    container.day = day
                    container.textView.text = day.date.dayOfMonth.toString()
                    if(day.owner == DayOwner.THIS_MONTH){
                        when{
                            day.date == selectedEndDay -> {
                                container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                                container.imageView.visibility = View.VISIBLE
                                Log.d("date","${day.date}")
                                Log.d("date","${day.date.dayOfMonth}")
                                Log.d("date","${day.date.month}")
                                Log.d("date","${day.date.dayOfWeek}")
                            }
                            else -> {
                                container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))
                                container.imageView.visibility = View.INVISIBLE
                            }
                        }
                    } else {
                        container.textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.line_grey))
                    }
                }
            }

            val currentMonth = YearMonth.now()
            val firstMonth = currentMonth.minusMonths(10)
            val lastMonth = currentMonth.plusMonths(10)
            binding.addScheduleEndCalendarCv.setup(firstMonth, lastMonth, daysOfWeek.first())
            binding.addScheduleEndCalendarCv.scrollToMonth(currentMonth)
        }


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
        binding.addScheduleStartDayTv.text = getDate(LocalDate.now())
        binding.addScheduleEndDayTv.text = getDate(LocalDate.now())

    }

    private fun getDate(date : LocalDate) : String {
        if(date.dayOfMonth < 10){
            return "${date.year}.${getMonth(date.month)}.0${date.dayOfMonth}(${getDayOfWeek(date.dayOfWeek)})"
        }
        return "${date.year}.${getMonth(date.month)}.${date.dayOfMonth}(${getDayOfWeek(date.dayOfWeek)})"
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

    private fun getAlarm(){
        if(binding.addScheduleAlarmNp.value != 0){
            binding.addScheduleAlarmMinuteTv.text = binding.addScheduleAlarmNp.value.toString()
            binding.addScheduleAlarmMinuteTv.visibility = View.VISIBLE
            binding.addScheduleAlarmAfterMinuteTv.visibility = View.VISIBLE
        } else {
            binding.addScheduleAlarmMinuteTv.visibility = View.GONE
            binding.addScheduleAlarmAfterMinuteTv.visibility = View.GONE
        }

    }

    private fun changeCalendar(calendarView: CalendarView, textView:TextView){
        binding.addScheduleStartTimePickerTp.visibility = View.GONE
        binding.addScheduleStartTimeBackgroundIv.visibility = View.GONE
        binding.addScheduleEndTimePickerTp.visibility = View.GONE
        binding.addScheduleAlarmNp.visibility = View.GONE
        binding.addScheduleAfterTv.visibility = View.GONE

        if(calendarView == binding.addScheduleStartCalendarCv){
            if(calendarView.visibility == View.VISIBLE){
                calendarView.visibility = View.GONE
                binding.addScheduleStartCalendarDateContainer.visibility = View.GONE
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))
            } else{
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.active))
                binding.addScheduleEndDayTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))
                calendarView.visibility = View.VISIBLE
                binding.addScheduleStartCalendarDateContainer.visibility = View.VISIBLE
                binding.addScheduleEndCalendarCv.visibility = View.GONE
                binding.addScheduleEndCalendarDateContainer.visibility = View.GONE
            }
        }
        else {
            if(calendarView.visibility == View.VISIBLE){
                calendarView.visibility = View.GONE
                binding.addScheduleEndCalendarDateContainer.visibility = View.GONE
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))
            } else{
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.active))
                binding.addScheduleStartDayTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))
                calendarView.visibility = View.VISIBLE
                binding.addScheduleEndCalendarDateContainer.visibility = View.VISIBLE
                binding.addScheduleStartCalendarCv.visibility = View.GONE
                binding.addScheduleStartCalendarDateContainer.visibility = View.GONE
            }
        }
    }

    private fun changeTimePicker(timePicker: TimePicker, textView: TextView) {
        changeText()
        binding.addScheduleStartCalendarCv.visibility = View.GONE
        binding.addScheduleStartCalendarDateContainer.visibility = View.GONE
        binding.addScheduleStartDayTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))
        binding.addScheduleEndCalendarCv.visibility = View.GONE
        binding.addScheduleEndCalendarDateContainer.visibility = View.GONE
        binding.addScheduleEndDayTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))
        binding.addScheduleAlarmNp.visibility = View.GONE
        binding.addScheduleAfterTv.visibility = View.GONE

        if(timePicker == binding.addScheduleStartTimePickerTp){
            if(timePicker.visibility == View.VISIBLE){
                timePicker.visibility = View.GONE
                binding.addScheduleStartTimeBackgroundIv.visibility = View.GONE
            } else{
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.active))
                timePicker.visibility = View.VISIBLE
                binding.addScheduleStartTimeBackgroundIv.visibility = View.VISIBLE
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
                binding.addScheduleStartTimeBackgroundIv.visibility = View.GONE
            }
        }
    }

    private fun numberPickerVisibility(){
        changeText()
        binding.addScheduleStartCalendarCv.visibility = View.GONE
        binding.addScheduleEndCalendarCv.visibility = View.GONE
        binding.addScheduleStartTimePickerTp.visibility = View.GONE
        binding.addScheduleStartTimeBackgroundIv.visibility = View.GONE
        binding.addScheduleEndTimePickerTp.visibility = View.GONE
        binding.addScheduleAlarmNp.visibility = View.GONE
        binding.addScheduleAfterTv.visibility = View.GONE

        val numberPicker = binding.addScheduleAlarmNp
        if(flag == 0){
            numberPicker.visibility = View.VISIBLE
            binding.addScheduleAfterTv.visibility = View.VISIBLE
            flag = 1
        } else{
            numberPicker.visibility = View.GONE
            binding.addScheduleAfterTv.visibility = View.GONE
            flag = 0
        }
    }

    private fun changeText() {
        getAlarm()
        if (binding.addScheduleStartCalendarCv.visibility == View.VISIBLE) {
            binding.addScheduleStartDayTv.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.line_black
                )
            )
        } else if (binding.addScheduleEndCalendarCv.visibility == View.VISIBLE) {
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


    private fun getDayOfWeek(dayOfWeek: DayOfWeek) : String{
        val stringDayOfWeek =
            when(daysOfWeek.indexOf(dayOfWeek)) {
                1 -> "월"
                2 -> "화"
                3 -> "수"
                4 -> "목"
                5 -> "금"
                6 -> "토"
                else -> "일"
            }
        return stringDayOfWeek
    }
    private fun getMonth(month : Month) : String{
        val stringMonth =
            when(month) {
                Month.JANUARY -> "01"
                Month.FEBRUARY -> "02"
                Month.MARCH -> "03"
                Month.APRIL -> "04"
                Month.MAY -> "05"
                Month.JUNE -> "06"
                Month.JULY -> "07"
                Month.AUGUST -> "08"
                Month.SEPTEMBER -> "09"
                Month.OCTOBER -> "10"
                Month.NOVEMBER -> "11"
                else -> "12"
            }
        return stringMonth
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
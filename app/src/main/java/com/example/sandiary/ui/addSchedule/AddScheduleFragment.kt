package com.example.sandiary.ui.addSchedule

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sandiary.MainActivity
import com.example.sandiary.R
import com.example.sandiary.Schedule
import com.example.sandiary.databinding.FragmentAddScheduleBinding
import com.example.sandiary.databinding.ItemCalendarDayBinding
import com.example.sandiary.function.ScheduleDatabase
import com.example.sandiary.ui.calendar.CalendarFragment
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.*

class AddScheduleFragment : Fragment() {
    private lateinit var addScheduleViewModel: AddScheduleViewModel
    private var _binding: FragmentAddScheduleBinding? = null
    private var scheduleDB : ScheduleDatabase? = null

    var startMonth : Int = 0
    var startDay : Int = 0
    var endMonth : Int = 0
    var endDay : Int = 0
    var startHour : Int = 0
    var startMinute : Int = 0
    var endHour : Int = 0
    var endMinute : Int = 0

    val timeZoneArray = arrayOf("오전", "오후")
    var currentTimeZone = 0
    var currentHour = LocalTime.now().hour
    val currentMinute = LocalTime.now().minute
    var currentMinuteString = ""
    var pickerFlag = 0

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
        binding.addScheduleWriteScheduleEt.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if(b){
                checkPickerVisibility()
                binding.addScheduleStartCalendarCv.visibility = View.GONE
                binding.addScheduleStartCalendarDateContainer.visibility = View.GONE
                binding.addScheduleEndCalendarCv.visibility = View.GONE
                binding.addScheduleEndCalendarDateContainer.visibility = View.GONE
            } else{
                hideKeyboard(binding.addScheduleWriteScheduleEt)
            }
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
                        when(day.date) {
                            selectedStartDay -> {
                                container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                                container.imageView.visibility = View.VISIBLE
                                startMonth = getMonth(day.date.month).toInt()
                                startDay = day.date.dayOfMonth
//                                if(endMonth < startMonth){
//                                    endMonth = startMonth+1
//                                    if(endDay < startDay){
//                                        startDay = endDay-1
//                                    }
//                                } else {
//                                    if(endDay < startDay){
//                                        startDay = endDay-1
//                                    }
//                                }
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
                        when(day.date) {
                            selectedEndDay -> {
                                container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                                container.imageView.visibility = View.VISIBLE
                                endMonth = getMonth(day.date.month).toInt()
                                endDay = day.date.dayOfMonth
//                                if(endMonth < startMonth){
//                                    startMonth = endMonth-1
//                                    if(endDay < startDay){
//                                        startDay = endDay-1
//                                    }
//                                } else {
//                                    if(endDay < startDay){
//                                        startDay = endDay-1
//                                    }
//                                }
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
           val schedule = Schedule(binding.addScheduleWriteScheduleEt.text.toString(),startMonth, startDay, endMonth ,endDay, 1, 3,2,4, null, null)
            CoroutineScope(Dispatchers.IO).launch {
                scheduleDB!!.scheduleDao().insertSchedule(schedule)
                Log.d("insertData","${startHour}, ${startMinute}")
            }
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, CalendarFragment()).commit()
        }

        binding.addScheduleStartTimeTv.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                clickedNumberPicker(binding.addScheduleStartTimeZonePickerNp, binding.addScheduleStartTimeTv)
            }
        }
        binding.addScheduleEndTimeTv.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                clickedNumberPicker(binding.addScheduleEndTimeZonePickerNp, binding.addScheduleEndTimeTv)
            }
        }
        binding.addScheduleStartDayTv.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                clickedCalendarView(binding.addScheduleStartCalendarCv, binding.addScheduleStartDayTv)
            }
        }
        binding.addScheduleEndDayTv.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                clickedCalendarView(binding.addScheduleEndCalendarCv, binding.addScheduleEndDayTv)
            }
        }
        binding.addScheduleAlarmTv.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                clickedNumberPicker(binding.addScheduleAlarmNp, binding.addScheduleAlarmTv)
            }
        }
        binding.addScheduleAllDaySw.setOnCheckedChangeListener { compoundButton, state ->  
            if(state){
                //checked
                binding.addScheduleStartTimeTv.isEnabled = false
                binding.addScheduleEndTimeTv.isEnabled = false

                binding.addScheduleStartTimeTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_grey))
                binding.addScheduleStartTimeTv.text = "오전 12:00"
                binding.addScheduleEndTimeTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_grey))
                binding.addScheduleEndTimeTv.text = "오후 11:59"
            } else {
                binding.addScheduleStartTimeTv.isEnabled = true
                binding.addScheduleEndTimeTv.isEnabled = true

                when(pickerFlag){
                    0 -> {
                        getTime()
                        binding.addScheduleStartTimeTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_grey))
                        binding.addScheduleEndTimeTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_grey))
                    }
                    else -> {
                        getTime()
                        getPickerValue(binding.addScheduleStartTimeZonePickerNp)
                        getPickerValue(binding.addScheduleEndTimeZonePickerNp)
                        binding.addScheduleStartTimeTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))
                        binding.addScheduleEndTimeTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))
                    }
                }
            }
        } 
        return root
    }
    private fun hideKeyboard(editText: EditText){
        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(editText.windowToken, 0)
        }
    }

    private fun initFragment(){
        scheduleDB = ScheduleDatabase.getInstance(requireContext())
        binding.addScheduleAlarmNp.minValue = 0
        binding.addScheduleAlarmNp.maxValue = 60
        binding.addScheduleStartDayTv.text = getDate(LocalDate.now())
        binding.addScheduleEndDayTv.text = getDate(LocalDate.now())
        initPicker()
    }

    private fun initPicker(){
        val hourArray = Array(12){
            if(it >= 9){
                "${it + 1}시"
            } else {
                "0${it + 1}시"
            }
        }
        val minuteArray = Array(61){
            if(it >= 9){
                "${it}분"
            } else {
                "0${it}분"
            }
        }
        getTime()

        binding.addScheduleStartTimeZonePickerNp.minValue = 0
        binding.addScheduleStartTimeZonePickerNp.maxValue = timeZoneArray.size-1
        binding.addScheduleStartTimeZonePickerNp.displayedValues = timeZoneArray
        binding.addScheduleStartTimeZonePickerNp.value = currentTimeZone

        binding.addScheduleStartHourPickerNp.minValue = 0
        binding.addScheduleStartHourPickerNp.maxValue = hourArray.size-1
        binding.addScheduleStartHourPickerNp.displayedValues = hourArray
        binding.addScheduleStartHourPickerNp.value = currentHour-1

        binding.addScheduleStartMinutePickerNp.minValue = 0
        binding.addScheduleStartMinutePickerNp.maxValue = minuteArray.size-1
        binding.addScheduleStartMinutePickerNp.displayedValues = minuteArray
        binding.addScheduleStartMinutePickerNp.value = currentMinute

        binding.addScheduleEndTimeZonePickerNp.minValue = 0
        binding.addScheduleEndTimeZonePickerNp.maxValue = timeZoneArray.size-1
        binding.addScheduleEndTimeZonePickerNp.displayedValues = timeZoneArray
        binding.addScheduleEndTimeZonePickerNp.value = currentTimeZone

        binding.addScheduleEndHourPickerNp.minValue = 0
        binding.addScheduleEndHourPickerNp.maxValue = hourArray.size-1
        binding.addScheduleEndHourPickerNp.displayedValues = hourArray
        binding.addScheduleEndHourPickerNp.value = currentHour-1

        binding.addScheduleEndMinutePickerNp.minValue = 0
        binding.addScheduleEndMinutePickerNp.maxValue = minuteArray.size-1
        binding.addScheduleEndMinutePickerNp.displayedValues = minuteArray
        binding.addScheduleEndMinutePickerNp.value = currentMinute

    }

    private fun showNumberPicker(numberPicker: NumberPicker){
        when(numberPicker){
            binding.addScheduleStartTimeZonePickerNp -> {
                binding.addScheduleStartTimeZonePickerNp.visibility = View.VISIBLE
                binding.addScheduleStartHourPickerNp.visibility = View.VISIBLE
                binding.addScheduleStartMinutePickerNp.visibility = View.VISIBLE
                binding.addScheduleStartTimeBackgroundIv.visibility = View.VISIBLE
            }
            binding.addScheduleEndTimeZonePickerNp -> {
                binding.addScheduleEndTimeZonePickerNp.visibility = View.VISIBLE
                binding.addScheduleEndHourPickerNp.visibility = View.VISIBLE
                binding.addScheduleEndMinutePickerNp.visibility = View.VISIBLE
                binding.addScheduleEndTimeBackgroundIv.visibility = View.VISIBLE
            }
            else -> {
                binding.addScheduleAlarmNp.visibility = View.VISIBLE
                binding.addScheduleAlarmBackgroundIv.visibility = View.VISIBLE
                binding.addScheduleAlarmBeforeMinuteTv.visibility = View.VISIBLE
                binding.addScheduleAlarmMinuteTv.visibility = View.VISIBLE
                binding.addScheduleAlarmNumberPickerTv.visibility = View.VISIBLE
            }
        }
    }

    private fun hideNumberPicker(numberPicker: NumberPicker){
        when(numberPicker){
            binding.addScheduleStartTimeZonePickerNp -> {
                binding.addScheduleStartTimeZonePickerNp.visibility = View.GONE
                binding.addScheduleStartHourPickerNp.visibility = View.GONE
                binding.addScheduleStartMinutePickerNp.visibility = View.GONE
                binding.addScheduleStartTimeBackgroundIv.visibility = View.GONE
            }
            binding.addScheduleEndTimeZonePickerNp -> {
                binding.addScheduleEndTimeZonePickerNp.visibility = View.GONE
                binding.addScheduleEndHourPickerNp.visibility = View.GONE
                binding.addScheduleEndMinutePickerNp.visibility = View.GONE
                binding.addScheduleEndTimeBackgroundIv.visibility = View.GONE
            }
            else -> {
                binding.addScheduleAlarmNp.visibility = View.GONE
                binding.addScheduleAlarmBackgroundIv.visibility = View.GONE
                binding.addScheduleAlarmNumberPickerTv.visibility = View.GONE
            }
        }
    }

    private fun clickedCalendarView(calendarView: CalendarView, textView:TextView){
        checkPickerVisibility()

        if(pickerFlag == 1){
            binding.addScheduleStartTimeTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))
            binding.addScheduleEndTimeTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))
        }

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
    private fun checkPickerVisibility(){
        when {
            (binding.addScheduleStartTimeZonePickerNp.visibility == View.VISIBLE) -> {
                getPickerValue(binding.addScheduleStartTimeZonePickerNp)
                hideNumberPicker(binding.addScheduleStartTimeZonePickerNp)
            }
            (binding.addScheduleEndTimeZonePickerNp.visibility == View.VISIBLE) -> {
                getPickerValue(binding.addScheduleEndTimeZonePickerNp)
                hideNumberPicker(binding.addScheduleEndTimeZonePickerNp)
            }
            (binding.addScheduleAlarmNp.visibility == View.VISIBLE) -> {
                getPickerValue(binding.addScheduleAlarmNp)
                hideNumberPicker(binding.addScheduleAlarmNp)
            }
        }
    }
    private fun clickedNumberPicker(numberPicker: NumberPicker, textView: TextView) {
        if(pickerFlag == 1){
            binding.addScheduleStartTimeTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))
            binding.addScheduleEndTimeTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))
        }

        binding.addScheduleStartCalendarCv.visibility = View.GONE
        binding.addScheduleStartCalendarDateContainer.visibility = View.GONE
        binding.addScheduleStartDayTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))

        binding.addScheduleEndCalendarCv.visibility = View.GONE
        binding.addScheduleEndCalendarDateContainer.visibility = View.GONE
        binding.addScheduleEndDayTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))

        when(numberPicker){
            binding.addScheduleStartTimeZonePickerNp -> {
                if(numberPicker.visibility == View.VISIBLE){
                    getPickerValue(numberPicker)
                    hideNumberPicker(numberPicker)
                    binding.addScheduleEndTimeTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))
                } else{
                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.active))
                    showNumberPicker(numberPicker)
                    hideNumberPicker(binding.addScheduleEndTimeZonePickerNp)
                    hideNumberPicker(binding.addScheduleAlarmNp)
                }
            }
            binding.addScheduleEndTimeZonePickerNp -> {
                if(numberPicker.visibility == View.VISIBLE){
                    hideNumberPicker(numberPicker)
                    getPickerValue(numberPicker)
                    binding.addScheduleStartTimeTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))
                } else{
                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.active))
                    showNumberPicker(numberPicker)
                    hideNumberPicker(binding.addScheduleStartTimeZonePickerNp)
                    hideNumberPicker(binding.addScheduleAlarmNp)
                }
            }
            else -> {
                if(numberPicker.visibility == View.VISIBLE){
                    hideNumberPicker(numberPicker)
                } else {
                    showNumberPicker(numberPicker)
                    hideNumberPicker(binding.addScheduleStartTimeZonePickerNp)
                    hideNumberPicker(binding.addScheduleEndTimeZonePickerNp)
                }
            }
        }
    }


    private fun getDayOfWeek(dayOfWeek: DayOfWeek): String {
        return when (daysOfWeek.indexOf(dayOfWeek)) {
            1 -> "월"
            2 -> "화"
            3 -> "수"
            4 -> "목"
            5 -> "금"
            6 -> "토"
            else -> "일"
        }
    }
    private fun getMonth(month: Month): String {
        return when (month) {
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
    }

    private fun getDate(date : LocalDate) : String {
        if(date.dayOfMonth < 10){
            return "${date.year}.${getMonth(date.month)}.0${date.dayOfMonth}(${getDayOfWeek(date.dayOfWeek)})"
        }
        return "${date.year}.${getMonth(date.month)}.${date.dayOfMonth}(${getDayOfWeek(date.dayOfWeek)})"
    }

    private fun getTime(){
        currentMinuteString = if(currentMinute >= 10){
            currentMinute.toString()
        } else {
            "0${currentMinute}"
        }
        if(currentHour >= 12){
            currentTimeZone = 1
            currentHour -= 12
        }

        binding.addScheduleStartTimeTv.text = "${timeZoneArray[currentTimeZone]} 0${currentHour}:${currentMinuteString}"
        binding.addScheduleEndTimeTv.text = "${timeZoneArray[currentTimeZone]} 0${currentHour}:${currentMinuteString}"
    }

    private fun getPickerValue(numberPicker: NumberPicker){
        var timezone = 0
        var hour = 0
        var minute = 0
        var time = ""
        var hourString = ""
        var minuteString = ""
        when(numberPicker){
            binding.addScheduleStartTimeZonePickerNp -> {
                pickerFlag = 1
                timezone = binding.addScheduleStartTimeZonePickerNp.value
                hour = binding.addScheduleStartHourPickerNp.value
                minute = binding.addScheduleStartMinutePickerNp.value

                startHour = hour+1
                startMinute = minute
                hourString = if(hour < 9){
                    "0${hour+1}"
                } else {
                    (hour+1).toString()
                }
                minuteString = if(minute < 10){
                    "0${minute}"
                } else {
                    minute.toString()
                }
                binding.addScheduleStartTimeTv.text = "${timeZoneArray[timezone]} ${hourString}:${minuteString}"
            }
            binding.addScheduleEndTimeZonePickerNp -> {
                pickerFlag = 1
                timezone = binding.addScheduleEndTimeZonePickerNp.value
                hour = binding.addScheduleEndHourPickerNp.value
                minute = binding.addScheduleEndMinutePickerNp.value

                endHour = hour+1
                endMinute = minute
                hourString = if(hour < 9){
                    "0${hour+1}"
                } else {
                    (hour+1).toString()
                }
                minuteString = if(minute < 10){
                    "0${minute}"
                } else {
                    minute.toString()
                }
                binding.addScheduleEndTimeTv.text = "${timeZoneArray[timezone]} ${hourString}:${minuteString}"
            }
            else -> {
                binding.addScheduleAlarmMinuteTv.text = binding.addScheduleAlarmNp.value.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



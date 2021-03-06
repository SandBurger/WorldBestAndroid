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
import com.example.sandiary.ui.addSchedule.AddScheduleFragment
import android.util.Log
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sandiary.Diary
import com.example.sandiary.Schedule
import com.example.sandiary.databinding.ItemCalendarDayBinding
import com.example.sandiary.function.PlanDatabase
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.*
import java.util.*

class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel
    //lateinit var binding : FragmentCalendarBinding
    private var _binding: FragmentCalendarBinding? = null
    private var planDB : PlanDatabase? = null
    private var selectedDay : LocalDate? = null
    private var year : Int = 0
    private var month : Int = 0
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
        initFragment()
        val root: View = binding.root
        val localDate = LocalDate.now()
        year = localDate.year
        month = localDate.month.value

        val day2 = binding.calendarDateTv
        calendarViewModel.text.observe(viewLifecycleOwner, Observer {
            day2.text = it
        })
        CoroutineScope(Dispatchers.IO).launch {
            planDB!!.planDao().getMonthPlan(3)
        }
        class DayViewContainer(view : View) : ViewContainer(view) {
            val textView = ItemCalendarDayBinding.bind(view).itemCalendarDayTv
            val imageView = ItemCalendarDayBinding.bind(view).itemCalendarDayIv
            lateinit var day : CalendarDay
            init {
//                binding.calendarSearchIb.setOnClickListener {
//                    Log.d("dd","dd")
//                    binding.calendarCalendarCv.notifyMonthChanged(YearMonth.of(2022,11))
//                    binding.calendarCalendarCv.scrollToMonth(YearMonth.of(2022,11))
//                }
                view.setOnClickListener{
                    if (day.owner == DayOwner.THIS_MONTH){
                        val currentSelection = selectedDay
                        if(currentSelection == day.date){
                            selectedDay = null
                            binding.calendarCalendarCv.notifyDateChanged(currentSelection)
                        } else {
                            selectedDay = day.date
                            //binding.calendarDateTv.text = day.date.toString()
                            binding.calendarCalendarCv.notifyDateChanged(day.date)
                            if (currentSelection != null){
                                binding.calendarCalendarCv.notifyDateChanged(currentSelection)
                            }
                        }
                    }
                }

            }
        }

        CoroutineScope(Dispatchers.Main).launch{
            binding.calendarCalendarCv.dayBinder = object : DayBinder<DayViewContainer> {
                override fun create(view: View) =  DayViewContainer(view)
                override fun bind(container: DayViewContainer, day: CalendarDay) {
                    container.day = day
                    container.textView.text = day.date.dayOfMonth.toString()
                    binding.calendarCalendarCv.monthScrollListener = { calendarMonth ->
                        binding.calendarDateTv.text = "${calendarMonth.year}??? ${calendarMonth.month}???"
                        year = calendarMonth.year
                        month = calendarMonth.month
                    }

                    if(day.owner == DayOwner.THIS_MONTH){
                        container.textView.setTextColor(ContextCompat
                            .getColor(requireContext(),R.color.line_black))
                        when{
                            day.date == selectedDay -> {
                                container.imageView.visibility = View.VISIBLE
                                Log.d("date","${day.date}")
                                Log.d("date","${day.date.dayOfMonth}")
                                Log.d("date","${day.date.month}")
                            }
                            else -> {
                                container.imageView.visibility = View.INVISIBLE
                            }
                        }
                    } else {
                        container.textView.setTextColor(ContextCompat
                            .getColor(requireContext(),R.color.line_grey))
                    }
                }
            }
            val daysOfWeek = arrayOf(
                DayOfWeek.SUNDAY,
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY
            )

            val currentMonth = YearMonth.now()
            Log.d("now","${currentMonth}")
            val firstMonth = currentMonth.minusMonths(100)
            val lastMonth = currentMonth.plusMonths(200)
            binding.calendarCalendarCv.setup(firstMonth, lastMonth, daysOfWeek.first())
            binding.calendarCalendarCv.scrollToMonth(currentMonth)
        }
        var dummyScheduleList = ArrayList<Schedule>()
        dummyScheduleList.add(Schedule("10??? ??????",10,30,12,20))
        dummyScheduleList.add(Schedule("11??? ??????",11,30,12,20))
        dummyScheduleList.add(Schedule("dasd",8,30,12,20))
        dummyScheduleList.sortBy { it.startHour }

        binding.calendarScheduleRv.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val scheduleRVAdapter = ScheduleRVAdapter(dummyScheduleList)
        scheduleRVAdapter.itemClickListener(object : ScheduleRVAdapter.ItemClickListener{
            override fun onClick(diary: Diary) {

            }
        })
        binding.calendarScheduleRv.adapter = scheduleRVAdapter


        binding.calendarSearchIb.setOnClickListener {
            initPicker()
        }

        //Log.d("title","${binding.calendarCalendarCv.settingsManager.isShowDaysOfWeekTitle}")
        binding.calendarFloatingBtn.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, AddScheduleFragment()).commit()
        }


        return binding.root
    }

    private fun initFragment(){
        planDB = PlanDatabase.getInstance(requireContext())
    }

    private fun initPicker(){
        val view = layoutInflater.inflate(R.layout.dialog_month_picker, null)
        val dialog = AlertDialog.Builder(requireContext()).setView(view).create()
        val yearPicker = view.findViewById<NumberPicker>(R.id.dialog_month_picker_year_np)
        val monthPicker = view.findViewById<NumberPicker>(R.id.dialog_month_picker_month_np)
        yearPicker.minValue = 1900
        yearPicker.maxValue = 2100
        yearPicker.value = year
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = month
        dialog.show()
    }
    private fun getMonth(string : String) : String {
        val month =
            when (string) {
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
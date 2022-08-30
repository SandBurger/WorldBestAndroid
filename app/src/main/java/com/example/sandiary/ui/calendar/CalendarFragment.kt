package com.example.sandiary.ui.calendar

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sandiary.MainActivity
import com.example.sandiary.R
import com.example.sandiary.databinding.FragmentCalendarBinding
import com.example.sandiary.ui.addSchedule.AddScheduleFragment
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sandiary.Schedule
import com.example.sandiary.databinding.ItemCalendarDayBinding
import com.example.sandiary.function.ScheduleDatabase
import com.google.android.material.appbar.AppBarLayout
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.*
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel
    //lateinit var binding : FragmentCalendarBinding
    private lateinit var scheduleRVAdapter : ScheduleRVAdapter
    private lateinit var invisibleImageView: ImageView
    private var _binding: FragmentCalendarBinding? = null
    private var scheduleDB : ScheduleDatabase? = null
    private var selectedDay : LocalDate? = null
    private var beforeDay : LocalDate? = null
    private var year : Int = 0
    private var month : Int = 0
    var scheduleList : List<Schedule>? = null
    var dayScheduleList = arrayListOf<Schedule>()
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
        selectedDay = localDate
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
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        binding.calendarCalendarCv.setupAsync(firstMonth, lastMonth, daysOfWeek.first()) {
            binding.calendarCalendarCv.scrollToMonth(currentMonth)
        }

        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener{ appbar, verticalOffset ->
            var scrollRange = -1
            if(scrollRange == -1){
                scrollRange = appbar.totalScrollRange
            }
            if(verticalOffset+appbar.totalScrollRange == 0){
                binding.calendarCollapsedDateTv.visibility = View.VISIBLE
                binding.calendarCollapsedMonthSelectorIb.visibility = View.VISIBLE

                binding.calendarExpandedDateTv.visibility = View.GONE
                binding.calendarExpandedMonthSelectorIb.visibility = View.GONE
            } else {
                binding.calendarCollapsedDateTv.visibility = View.GONE
                binding.calendarCollapsedMonthSelectorIb.visibility = View.GONE

                binding.calendarExpandedDateTv.visibility = View.VISIBLE
                binding.calendarExpandedMonthSelectorIb.visibility = View.VISIBLE
            }
        })
        val onScrollListener = object :RecyclerView.OnScrollListener(){
            var temp = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(temp == 1){
                    super.onScrolled(recyclerView, dx, dy)
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                temp = 1
            }
        }
        binding.calendarScheduleRv.setOnScrollListener(onScrollListener)

        val day2 = binding.calendarCollapsedDateTv
        calendarViewModel.text.observe(viewLifecycleOwner, Observer {
            day2.text = it
        })
        binding.calendarScheduleRv.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        scheduleRVAdapter = ScheduleRVAdapter(dayScheduleList)
        scheduleRVAdapter.notifyDataSetChanged()
        binding.calendarScheduleRv.adapter = scheduleRVAdapter

        binding.calendarCollapsedMonthSelectorIb.setOnClickListener {
            showPicker()
        }
        binding.calendarExpandedMonthSelectorIb.setOnClickListener {
            showPicker()
        }

        class DayViewContainer(view : View) : ViewContainer(view) {
            val textView = ItemCalendarDayBinding.bind(view).itemCalendarDayTv
            val indicator = ItemCalendarDayBinding.bind(view).itemCalendarDayIndicatorIv

            lateinit var day : CalendarDay

            init {
                view.setOnClickListener{
                    if (day.owner == DayOwner.THIS_MONTH){
                        val currentSelection = selectedDay
                        if(currentSelection == day.date){
                            selectedDay = null
                            binding.calendarCalendarCv.notifyDateChanged(currentSelection)
                            dayScheduleList.clear()
                            scheduleRVAdapter.notifyDataSetChanged()
                        } else {
                            selectedDay = day.date
                            binding.calendarCalendarCv.notifyDateChanged(day.date)
                            if (currentSelection != null){
                                binding.calendarCalendarCv.notifyDateChanged(currentSelection)

                            }
                        }
                    }
                }

            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            binding.calendarCalendarCv.dayBinder = object : DayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)
                override fun bind(container: DayViewContainer, day: CalendarDay) {
                    container.day = day
                    val textView = container.textView
                    textView.text = day.date.dayOfMonth.toString()
                    scheduleList = getMonthSchedule(day.date.monthValue)

                    if (day.owner == DayOwner.THIS_MONTH) {
                        container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))

                        if(scheduleList!=null){
                            for(i in scheduleList!!){
                                if(i.startMonth == day.date.monthValue && i.startDay == day.date.dayOfMonth){
                                    container.indicator.visibility = View.VISIBLE
                                    if(container.day.date < LocalDate.now()){
                                        ImageViewCompat.setImageTintList(container.indicator,
                                            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.line_grey)))
                                    }
                                }
                            }
                        } else {
                            if(container.indicator.visibility == View.VISIBLE){
                                container.indicator.visibility = View.INVISIBLE
                            }
                        }
                        when {
                            day.date == selectedDay -> {
                                if (container.indicator.visibility == View.VISIBLE) {
                                    container.indicator.visibility = View.INVISIBLE
                                }

                                dayScheduleList.clear()
                                if (scheduleList != null) {
                                    for (schedule in scheduleList!!) {
                                        if (schedule.startDay.toString() == container.textView.text) {
                                            dayScheduleList.add(schedule)
                                        }
                                    }
                                    if(dayScheduleList.isEmpty()){
                                        binding.calendarEmptyTv.visibility = View.VISIBLE
                                    } else {
                                        binding.calendarEmptyTv.visibility = View.GONE
                                    }
                                }
                                scheduleRVAdapter.notifyDataSetChanged()
                                container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.indicator_active))
                            }
                            else -> {
                                container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.line_black))
                            }
                        }
                    } else {
                        container.textView.setTextColor(
                            ContextCompat
                                .getColor(requireContext(), R.color.line_grey)
                        )
                    }
                }
            }
        }

        binding.calendarCalendarCv.monthScrollListener = { calendarMonth ->
            binding.calendarExpandedDateTv.text = "${calendarMonth.year}년 ${calendarMonth.month}월"
            binding.calendarCollapsedDateTv.text = "${calendarMonth.year}년 ${calendarMonth.month}월"
            year = calendarMonth.year
            month = calendarMonth.month
            dayScheduleList.clear()
            scheduleRVAdapter.notifyDataSetChanged()
        }



        var dummyScheduleList = ArrayList<Schedule>()

        dummyScheduleList.sortBy { it.startHour }


        scheduleRVAdapter.itemClickListener(object : ScheduleRVAdapter.ItemClickListener{
            override fun onClick(schedule: Schedule, holder : ScheduleRVAdapter.ViewHolder) {
                showPopup(holder)
            }
        })


        //Log.d("title","${binding.calendarCalendarCv.settingsManager.isShowDaysOfWeekTitle}")
        binding.calendarFloatingBtn.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, AddScheduleFragment()).commit()
        }


        return binding.root
    }
    private fun getMonthSchedule(month : Int): List<Schedule>? {
        //var totalScheduleList = arrayListOf<Schedule>()
//        CoroutineScope(Dispatchers.IO).launch {
//            totalScheduleList =
//                scheduleDB!!.scheduleDao().getMonthSchedule(month)
//            Log.d("total", "${totalScheduleList}")
//            dayScheduleList = totalScheduleList
//        }
//        for(i in totalScheduleList){
//            dayScheduleList.add(i)
//        }
//        scheduleRVAdapter.notifyDataSetChanged()
        var totalScheduleList =
            when(month) {
                8 -> listOf<Schedule>(Schedule("dummy",2022,8,14,2022,12,14,1,2,2,3,null,null), Schedule("dummy2",2022, 8,14,2022,12,14,1,2,2,3,null, null))
                9 -> listOf<Schedule>(Schedule("dummy",2022, 9,15,2022,12,14,1,2,2,3,null,null))
                else -> null
            }
        return totalScheduleList
    }

    private fun initFragment(){
        scheduleDB = ScheduleDatabase.getInstance(requireContext())
    }

    private fun showPicker(){
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

        view.findViewById<TextView>(R.id.dialog_month_picker_confirm_tv).setOnClickListener {
            year = yearPicker.value
            month = monthPicker.value
            binding.calendarCalendarCv.notifyMonthChanged(YearMonth.of(year,month))
            binding.calendarCalendarCv.scrollToMonth(YearMonth.of(year,month))
            dialog.dismiss()
        }
        view.findViewById<TextView>(R.id.dialog_month_picker_cancel_tv).setOnClickListener {
            dialog.dismiss()
        }
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

    private fun showPopup(holder : ScheduleRVAdapter.ViewHolder){
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.popup_schedule, null)
        val popup = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

        popup.showAsDropDown(holder.binding.itemScheduleOptionIb, -200,-20)

        popup.isOutsideTouchable = true
        //popup.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        view.findViewById<ConstraintLayout>(R.id.popup_schedule_adjust_lo).setOnClickListener {
            popup.dismiss()
        }
        view.findViewById<ConstraintLayout>(R.id.popup_schedule_remove_lo).setOnClickListener {
            popup.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
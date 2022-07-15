package com.example.sandiary

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.sandiary.databinding.ItemCalendarDayBinding
import com.example.sandiary.databinding.TestBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class TestDialog : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않는다.
        isCancelable = true
    }

    private var selectedDay : LocalDate? = null
    private lateinit var pickerBinding : TestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pickerBinding = TestBinding.inflate(inflater, container, false)
        class DayViewContainer(view : View) : ViewContainer(view) {
            val textView = ItemCalendarDayBinding.bind(view).itemCalendarDayTv
            val imageView = ItemCalendarDayBinding.bind(view).itemCalendarDayIv
            lateinit var day : CalendarDay
            init {
                view.setOnClickListener{
                    if (day.owner == DayOwner.THIS_MONTH){
                        val currentSelection = selectedDay
                        if(currentSelection == day.date){
                            selectedDay = null
                            pickerBinding.dialogSeeAllV.notifyDateChanged(currentSelection)
                        } else {
                            selectedDay = day.date
                            pickerBinding.dialogSeeAllV.notifyDateChanged(day.date)
                            if (currentSelection != null){
                                pickerBinding.dialogSeeAllV.notifyDateChanged(currentSelection)
                            }
                        }
                    }
                }
            }
        }
        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.findViewById<TextView>(R.id.calendar_header_month_tv)
        }
        pickerBinding.dialogSeeAllV.monthHeaderBinder = object :
            MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                container.textView.text = "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            pickerBinding.dialogSeeAllV.dayBinder = object :
                DayBinder<DayViewContainer> {
                override fun create(view: View) =  DayViewContainer(view)
                override fun bind(container: DayViewContainer, day: CalendarDay) {
                    container.day = day
                    container.textView.text = day.date.dayOfMonth.toString()
                    if(day.owner == DayOwner.THIS_MONTH){
                        when{
                            day.date == selectedDay -> {
                                container.imageView.visibility = View.VISIBLE
                                Log.d("date","${day.date}")
                                Log.d("date","${day.date.dayOfMonth}")
                                Log.d("date","${day.date.month}")
                            }
                            day.date.dayOfWeek.value == 7 -> {
                                container.textView.setTextColor(
                                    ContextCompat
                                        .getColor(requireContext(), R.color.error))
                            }
                            day.date.dayOfWeek.value == 6 -> {
                                container.textView.setTextColor(
                                    ContextCompat
                                        .getColor(requireContext(), R.color.saturday_blue))
                            }
                            else -> {
                                container.textView.setTextColor(
                                    ContextCompat
                                        .getColor(requireContext(), R.color.line_black))
                                container.imageView.visibility = View.INVISIBLE
                            }
                        }
                    } else {
                        container.textView.setTextColor(
                            ContextCompat
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
            val firstMonth = currentMonth.minusMonths(10)
            val lastMonth = currentMonth.plusMonths(10)
            pickerBinding.dialogSeeAllV.setup(firstMonth, lastMonth, daysOfWeek.first())
            pickerBinding.dialogSeeAllV.scrollToMonth(currentMonth)
        }
        val datePickerView = layoutInflater.inflate(R.layout.test, null)
        val datePickerDialog = AlertDialog.Builder(requireContext()).setView(datePickerView)
            .setPositiveButton(R.string.thursday, DialogInterface.OnClickListener { dialogInterface, i ->
                dialog?.hide()
            })
            .setNegativeButton(R.string.thursday, DialogInterface.OnClickListener{dialogInterface, i ->
                dialog?.hide()  
            }).create()
        pickerBinding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        datePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return pickerBinding.root
    }

//    override fun onResume() {
//        super.onResume()
//        val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        val params : ViewGroup.LayoutParams? = dialog?.window?.attributes
//
//        params?.width = WindowManager.LayoutParams.WRAP_CONTENT
//        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
//        dialog?.window?.attributes = params as WindowManager.LayoutParams
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}
package com.example.sandiary

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.sandiary.databinding.DialogDatePickerBinding
import com.example.sandiary.databinding.ItemAddScheduleDayBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ClassCastException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth

class DialogDatePickerFragment : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않는다.
        isCancelable = true
    }

    private var selectedDay : LocalDate? = null
    private lateinit var binding : DialogDatePickerBinding
    private lateinit var month : YearMonth
    private val viewModel : SeeAllViewModel by activityViewModels()
    internal lateinit var listener : DialogClickListener

    interface DialogClickListener {
        fun onDialogPositive(dialog: DialogFragment)
        fun onDialogNegative(dialog: DialogFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogDatePickerBinding.inflate(inflater, container, false)

        class DayViewContainer(view : View) : ViewContainer(view) {
            val textView = ItemAddScheduleDayBinding.bind(view).itemAddScheduleDayTv
            val imageView = ItemAddScheduleDayBinding.bind(view).itemAddScheduleDayIv
            lateinit var day : CalendarDay
            init {
                view.setOnClickListener{
                    if (day.owner == DayOwner.THIS_MONTH){
                        val currentSelection = selectedDay
                        if(currentSelection == day.date){
                            selectedDay = null
                            binding.dialogDatePickerCalendarCv.notifyDateChanged(currentSelection)
                        } else {
                            selectedDay = day.date
                            binding.dialogDatePickerCalendarCv.notifyDateChanged(day.date)
                            if (currentSelection != null){
                                binding.dialogDatePickerCalendarCv.notifyDateChanged(currentSelection)
                            }
                        }
                    }
                }
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            binding.dialogDatePickerCalendarCv.dayBinder = object :
                DayBinder<DayViewContainer> {
                override fun create(view: View) =  DayViewContainer(view)
                override fun bind(container: DayViewContainer, day: CalendarDay) {
                    container.day = day
                    container.textView.text = day.date.dayOfMonth.toString()
                    if(day.owner == DayOwner.THIS_MONTH){
                        when{
                            day.date == selectedDay -> {
                                container.imageView.visibility = View.VISIBLE
                                container.textView.setTextColor(getColor(requireContext(), R.color.white))
                                Log.d("date","${day.date}")
                                Log.d("date","${day.date.dayOfMonth}")
                                Log.d("date","${day.date.month}")
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
            month = currentMonth
            val firstMonth = currentMonth.minusMonths(10)
            val lastMonth = currentMonth.plusMonths(10)
            binding.dialogDatePickerCalendarCv.setup(firstMonth, lastMonth, daysOfWeek.first())
            binding.dialogDatePickerCalendarCv.scrollToMonth(currentMonth)

            binding.dialogDatePickerPreviousIb.setOnClickListener {
                binding.dialogDatePickerCalendarCv.scrollToMonth(month.minusMonths(1))
            }
            binding.dialogDatePickerNextIb.setOnClickListener {
                binding.dialogDatePickerCalendarCv.scrollToMonth(month.plusMonths(1))
            }
        }


        binding.dialogDatePickerCalendarCv.monthScrollListener = { calendarMonth ->
            binding.dialogDatePickerDateTv.text = "${calendarMonth.year}년 ${calendarMonth.month}월"
            month = calendarMonth.yearMonth
            selectedDay = null
        }
        val datePickerView = layoutInflater.inflate(R.layout.dialog_date_picker, null)
        val datePickerDialog = AlertDialog.Builder(requireContext()).setView(datePickerView).create()

        datePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dialogDatePickerConfirmTv.setOnClickListener {
            viewModel.selectDate(selectedDay)
            listener.onDialogPositive(this)
            dialog?.dismiss()
        }
        binding.dialogDatePickerCancelTv.setOnClickListener {
            listener.onDialogNegative(this)
            dialog?.dismiss()
        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val percent = 95.toFloat() / 100
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent
        dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as DialogClickListener
        } catch (e : ClassCastException){
            throw ClassCastException()
        }
    }


}
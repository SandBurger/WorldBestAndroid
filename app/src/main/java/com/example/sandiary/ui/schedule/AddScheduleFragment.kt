package com.example.sandiary.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sandiary.MainActivity
import com.example.sandiary.R
import com.example.sandiary.databinding.FragmentAddScheduleBinding
import com.example.sandiary.ui.calendar.CalendarFragment
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
        val date = binding.addScheduleStartCalendarCv.selectedDates
        binding.addScheduleStartCalendarCv.onFocusChangeListener = View.OnFocusChangeListener{ _, p1->
            if(p1){
            } else{
                binding.addScheduleStartCalendarCv.visibility = View.GONE
                if (date.isNotEmpty()) {
                    Log.d("test", "teta")
                    val startdate = date.get(0)
                    val day = startdate.get(Calendar.DAY_OF_MONTH)
                    binding.addScheduleStartDayTv.text = day.toString()
                }
            }
        }


        binding.addScheduleStartCalendarCv.setOnClickListener {
            val calendar = binding.addScheduleStartCalendarCv.selectedDays
            Log.d("date", "${calendar}")
        }
        binding.addScheduleStartDayTv.setOnClickListener {
            showCalendar()
        }

        return root
    }
    private fun showCalendar(){
        binding.addScheduleStartCalendarCv.visibility = View.VISIBLE
    }

    private fun hideCalendar(){
        binding.addScheduleStartCalendarCv.visibility = View.INVISIBLE
    }
}
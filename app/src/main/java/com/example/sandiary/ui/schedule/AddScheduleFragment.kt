package com.example.sandiary.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sandiary.MainActivity
import com.example.sandiary.R
import com.example.sandiary.databinding.FragmentAddScheduleBinding
import com.example.sandiary.ui.calendar.CalendarFragment

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

        binding.scheduleExitIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, CalendarFragment()).commit()
        }
        val dateTv = binding.scheduleDateTv
        addScheduleViewModel.text.observe(viewLifecycleOwner, Observer {
            dateTv.text = it
        })

        return root
    }
}
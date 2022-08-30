package com.example.sandiary.ui.settings

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sandiary.DialogDatePickerFragment
import com.example.sandiary.R
import com.example.sandiary.databinding.FragmentSettingsBinding
import java.lang.ClassCastException

class SettingsFragment : Fragment() {
//    lateinit var binding : FragmentSettingsBinding
    private var _binding: FragmentSettingsBinding? = null

//    // This property is only valid between onCreateView and
//    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        parentFragmentManager.beginTransaction().add(R.id.settings_container, SettingsMenuFragment(), "menu")
            .commit()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //_binding = null
    }


}
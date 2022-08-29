package com.example.sandiary.ui.settings

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sandiary.MainActivity
import com.example.sandiary.R
import com.example.sandiary.databinding.FragmentSettingsAccountBinding
import com.example.sandiary.databinding.FragmentSettingsBinding

class SettingsAccountFragment : Fragment() {
    private val settingsViewModel: SettingsViewModel by activityViewModels()
    private var _binding: FragmentSettingsAccountBinding? = null

//    // This property is only valid between onCreateView and
//    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        _binding = FragmentSettingsAccountBinding.inflate(inflater, container, false)
        settingsViewModel.currentFragment("settings")
        val root: View = binding.root
        
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //_binding = null
    }
}
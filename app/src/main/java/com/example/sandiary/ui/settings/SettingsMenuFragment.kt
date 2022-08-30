package com.example.sandiary.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sandiary.R
import com.example.sandiary.databinding.FragmentSettingsBinding
import com.example.sandiary.databinding.FragmentSettingsMenuBinding

class SettingsMenuFragment : Fragment() {

    private var _binding: FragmentSettingsMenuBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        _binding = FragmentSettingsMenuBinding.inflate(inflater, container, false)

        val root: View = binding.root
        binding.settingsMenuProfileLo.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.settings_container, SettingsAccountFragment())
            .addToBackStack("menu")
            .commit()
        }
        
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //_binding = null
    }
}
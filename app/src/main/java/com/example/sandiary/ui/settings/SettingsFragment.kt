package com.example.sandiary.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.fragment.app.transaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sandiary.R
import com.example.sandiary.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
//    lateinit var binding : FragmentSettingsBinding
    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

//    // This property is only valid between onCreateView and
//    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)
//        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.settingsTitleTv
        settingsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
            println(it)
            if(it == "settings"){
                binding.settingsExitIb.visibility = View.VISIBLE
            }
        })
        parentFragmentManager.beginTransaction().replace(R.id.settings_container, SettingsMenuFragment())
            .commit()
        val my = parentFragmentManager.findFragmentByTag("Account")
        if(my != null && my.isVisible){
            binding.settingsExitIb.visibility = View.VISIBLE
        }

        binding.settingsExitIb.setOnClickListener {

        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //_binding = null
    }
}
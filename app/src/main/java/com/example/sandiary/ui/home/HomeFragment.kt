package com.example.sandiary.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sandiary.R
import com.example.sandiary.Util
import com.example.sandiary.WriteActivity
import com.example.sandiary.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    //lateinit var binding : FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
//        binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.homeWritingBox1Iv.setOnClickListener {
            startActivity(Intent(activity, WriteActivity::class.java))
        }
        val dateTv = binding.homeDateTv
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            dateTv.text = it
        })
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
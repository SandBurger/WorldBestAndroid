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
import com.example.sandiary.*
import com.example.sandiary.databinding.FragmentHomeBinding
import com.example.sandiary.ui.search.SearchFragment

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
        binding.homeSearchIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, SearchFragment()).commit()
        }
//        binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.homeWritingBox1Iv.setOnClickListener {
            startActivity(Intent(activity, WriteActivity::class.java))
        }
        binding.homeWritingBox2Iv.setOnClickListener {
            startActivity(Intent(activity, WriteActivity::class.java))
        }
        binding.homeGatherBtn.setOnClickListener {
            startActivity(Intent(activity, SeeAllActivity::class.java))
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
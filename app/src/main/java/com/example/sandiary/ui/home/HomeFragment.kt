package com.example.sandiary.ui.home

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.WindowCompat
import androidx.core.widget.ImageViewCompat
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
        checkWriting()
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

    private fun checkWriting(){
        if(binding.homeWritingContent1Tv.text == "글 쓰러 가기"){
            ImageViewCompat.setImageTintList(binding.homeWritingBox1Iv,
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.inactive_box_transparent_50)))
            binding.homeWritingContent1Tv.setTextColor(ContextCompat
               .getColor(requireContext(),R.color.line_grey))
            binding.homeWritingTime1Tv.setTextColor(ContextCompat
               .getColor(requireContext(),R.color.line_grey))

        }
        if(binding.homeWritingContent2Tv.text == "글 쓰러 가기"){
            ImageViewCompat.setImageTintList(binding.homeWritingBox2Iv,
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.inactive_box_transparent_50)))
           binding.homeWritingContent2Tv.setTextColor(ContextCompat
               .getColor(requireContext(),R.color.line_grey))
           binding.homeWritingTime2Tv.setTextColor(ContextCompat
               .getColor(requireContext(),R.color.line_grey))
       }
        if(binding.homeWritingContent3Tv.text == "글 쓰러 가기"){
            ImageViewCompat.setImageTintList(binding.homeWritingBox3Iv,
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.inactive_box_transparent_50)))
           binding.homeWritingContent3Tv.setTextColor(ContextCompat
               .getColor(requireContext(),R.color.line_grey))
           binding.homeWritingTime3Tv.setTextColor(ContextCompat
               .getColor(requireContext(),R.color.line_grey))
       }
    }
}
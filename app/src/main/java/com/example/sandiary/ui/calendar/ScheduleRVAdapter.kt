package com.example.sandiary.ui.calendar

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sandiary.Diary
import com.example.sandiary.R
import com.example.sandiary.Schedule
import com.example.sandiary.databinding.ItemScheduleBinding
import java.time.LocalTime
import java.util.*
import java.util.Arrays.toString

class ScheduleRVAdapter(val scheduleList : ArrayList<Schedule>) : RecyclerView.Adapter<ScheduleRVAdapter.ViewHolder>()  {
        lateinit var context: Context
        private lateinit var itemClickListener : ItemClickListener
        interface ItemClickListener{
            fun onClick(schedule: Schedule)
        }
        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            context = recyclerView.context
        }
        fun itemClickListener(mitemClickListener: ItemClickListener){
            itemClickListener = mitemClickListener
        }
        override fun getItemCount(): Int {
            return scheduleList.size
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding : ItemScheduleBinding = ItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(scheduleList[position])
            holder.binding.itemScheduleTitleTv.setOnClickListener {
                itemClickListener.onClick(scheduleList[position])
            }
        }
        inner class ViewHolder(val binding : ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root){
            fun bind(schedule : Schedule){
                if(schedule.startHour == null){

                } else if(LocalTime.now().hour < schedule.startHour!!){
                    binding.itemScheduleTitleTv.setTextColor(ContextCompat.getColor(context, R.color.line_black))
                    binding.itemScheduleIndicatorIv.setColorFilter(ContextCompat.getColor(context, R.color.indicator_active))
                }else{
                    binding.itemScheduleTitleTv.setTextColor(ContextCompat.getColor(context, R.color.font_grey))
                }
                //binding.itemScheduleTitleTv.text = schedule.scheduleName
                binding.itemScheduleTitleTv.text = schedule.startDay.toString()
            }
        }
    }

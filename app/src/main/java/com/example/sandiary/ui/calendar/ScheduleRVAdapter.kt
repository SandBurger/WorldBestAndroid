package com.example.sandiary.ui.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.core.widget.ImageViewCompat
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
            fun onClick(schedule: Schedule, holder : ViewHolder)
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
            holder.binding.itemScheduleOptionIb.setOnClickListener {
                itemClickListener.onClick(scheduleList[position], holder)
            }

        }
        inner class ViewHolder(val binding : ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root){
            fun bind(schedule : Schedule){
                if(schedule.startHour == null){

                } else if(LocalTime.now().hour < schedule.startHour!!){
                    binding.itemScheduleTitleTv.setTextColor(ContextCompat.getColor(context, R.color.line_black))
                    binding.itemScheduleIndicatorIv.setColorFilter(ContextCompat.getColor(context, R.color.indicator_active))
                    binding.itemScheduleTitleTv.setTextColor(ContextCompat.getColor(context, R.color.line_black))
                }else{
                    ImageViewCompat.setImageTintList(binding.itemScheduleOptionIb,
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.cardview_grey)))
                }

                binding.itemScheduleTitleTv.text = schedule.scheduleName
            }
        }
    }

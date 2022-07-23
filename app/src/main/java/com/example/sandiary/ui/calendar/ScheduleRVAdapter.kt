package com.example.sandiary.ui.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sandiary.Diary
import com.example.sandiary.Schedule
import com.example.sandiary.databinding.ItemScheduleBinding

class ScheduleRVAdapter(val scheduleList : ArrayList<Schedule>) : RecyclerView.Adapter<ScheduleRVAdapter.ViewHolder>()  {
        lateinit var context: Context
        private lateinit var itemClickListener : ItemClickListener
        interface ItemClickListener{
            fun onClick(diary: Diary)
        }
        //    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
//        super.onAttachedToRecyclerView(recyclerView)
//        context = recyclerView.context
//    }
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
//            holder.binding.item.setOnClickListener {
//                itemClickListener.onClick(diaryList[position])
//            }
        }
        inner class ViewHolder(val binding : ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root){
            fun bind(schedule : Schedule){
                binding.itemScheduleTitleTv.text = schedule.schedule
            }
        }
    }

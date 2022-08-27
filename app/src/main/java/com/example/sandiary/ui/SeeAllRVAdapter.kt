package com.example.sandiary.ui

import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sandiary.Diary
import com.example.sandiary.databinding.ItemDiaryBinding

class SeeAllRVAdapter(val diaryList : ArrayList<Diary>) : RecyclerView.Adapter<SeeAllRVAdapter.ViewHolder>() {
    lateinit var context: Context
    private lateinit var itemClickListener : ItemClickListener
    interface ItemClickListener{
        fun onRemove(diary: Diary)
        fun onClick(position: Int, diary : String?)
    }
//    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
//        super.onAttachedToRecyclerView(recyclerView)
//        context = recyclerView.context
//    }
    fun itemClickListener(mitemClickListener: ItemClickListener){
        itemClickListener = mitemClickListener
    }
    override fun getItemCount(): Int {
        return diaryList.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemDiaryBinding = ItemDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(diaryList[position])
        holder.binding.itemDiarySettingIb.setOnClickListener {
            itemClickListener.onRemove(diaryList[position])
        }
        holder.binding.itemDiaryBackgroundCv.setOnClickListener {
            itemClickListener.onClick(position, diaryList[position].diary)
        }
    }
    inner class ViewHolder(val binding : ItemDiaryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(diary : Diary){
            binding.itemDiaryDiaryTv.text = diary.diary
        }
    }
}



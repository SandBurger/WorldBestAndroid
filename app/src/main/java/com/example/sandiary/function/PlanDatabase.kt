package com.example.sandiary.function

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sandiary.Plan

@Database(entities = [Plan::class], version = 1)
abstract class PlanDatabase : RoomDatabase() {
    abstract fun planDao() : PlanDao

    companion object {
        private var instance : PlanDatabase? = null

        @Synchronized
        fun getInstance(context : Context) : PlanDatabase? {
            if(instance == null){
                synchronized(PlanDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PlanDatabase::class.java,
                        "plan-database"
                    ).build()
                }
            }

            return instance
        }
    }

}
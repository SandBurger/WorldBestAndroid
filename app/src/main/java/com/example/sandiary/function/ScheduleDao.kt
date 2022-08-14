package com.example.sandiary.function

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sandiary.Schedule
import java.util.ArrayList

@Dao
interface ScheduleDao {
    @Insert
    fun insertSchedule(schedule : Schedule)

    @Query("SELECT * FROM ScheduleTable")
    fun getAllSchedule() : List<Schedule>

    @Query("SELECT * FROM ScheduleTable WHERE startMonth = :startMonth")
    fun getMonthSchedule(startMonth : Int) : List<Schedule>

//    @Query("SELECT * FROM PlanTable WHERE ")
}
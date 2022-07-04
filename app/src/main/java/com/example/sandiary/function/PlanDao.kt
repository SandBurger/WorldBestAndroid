package com.example.sandiary.function

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sandiary.Plan

@Dao
interface PlanDao {
    @Insert
    fun insertPlan(plan : Plan)

    @Query("SELECT * FROM PlanTable")
    fun getAllPlan() : List<Plan>

}
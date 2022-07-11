package com.example.sandiary

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PlanTable")
data class Plan(
    var planName : String,
    var startMonth : Int,
    var startDay : Int,
    var endDate: Int,
    var startTime : String,
    var endTime : String,
    var contents : String
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}

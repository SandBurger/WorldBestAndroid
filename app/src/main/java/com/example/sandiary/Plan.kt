package com.example.sandiary

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PlanTable")
data class Plan(
    var planName : String,
    var startTime : String,
    var endTime : String,
    var contents : String
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}

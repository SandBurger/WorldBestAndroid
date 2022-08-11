package com.example.sandiary

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ScheduleTable")
data class Schedule(
    var scheduleName : String,
    var startMonth : Int,
    var startDay : Int,
    var endMonth: Int,
    var endDay: Int,
    var startHour : Int,
    var startMinute : Int,
    var endHour : Int,
    var endMinute : Int,
    var alarm : Int?,
    var memo : String?
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}

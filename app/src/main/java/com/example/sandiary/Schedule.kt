package com.example.sandiary

import java.time.LocalDateTime

data class Schedule(
    var schedule : String,
    var startHour : Int?,
    var startMinute : Int?,
    var endHour : Int?,
    var endMinute : Int?
)

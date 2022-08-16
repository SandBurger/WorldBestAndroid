package com.example.sandiary

import com.google.gson.annotations.SerializedName

data class Diary(
    @SerializedName("position") var position : Int,
    @SerializedName("diary") var diary : String?
)

package com.example.sandiary.response

import com.google.gson.annotations.SerializedName

data class DiaryResponse(
    @SerializedName("body") val body : Body,
    @SerializedName("status") val status : String
)

data class Body(
    @SerializedName("diary") val diary : String,
    @SerializedName("id") val id : Int,
    @SerializedName("sequence") val sequence : Int
)
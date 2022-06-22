package com.example.sandiary.function


import com.example.sandiary.response.DiaryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DiaryInterface {
    @GET("/diary/{id}")
    fun getDiary(
        @Path("id") id : Int
    ) : Call<DiaryResponse>

}
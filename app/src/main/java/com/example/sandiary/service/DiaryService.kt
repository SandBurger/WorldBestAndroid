package com.example.sandiary.service

import com.example.sandiary.function.DiaryInterface
import com.example.sandiary.response.DiaryResponse
import retrofit2.*
import android.util.Log
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ObjectInputStream

class DiaryService {
    fun getDiary(id:Int){
        val retrofit = Retrofit.Builder().baseUrl("http://sandburg-test-elb-1207633973.ap-northeast-2.elb.amazonaws.com/")
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create()).build()
        val diaryInterface = retrofit.create(DiaryInterface::class.java)
        diaryInterface.getDiary(id).enqueue(object : Callback<DiaryResponse>{
            override fun onResponse(call: Call<DiaryResponse>, response: Response<DiaryResponse>) {
                Log.d("getDiary", "${response.code()}")
                val resp = response.body()
            }

            override fun onFailure(call: Call<DiaryResponse>, t: Throwable) {

            }
        })
    }
}
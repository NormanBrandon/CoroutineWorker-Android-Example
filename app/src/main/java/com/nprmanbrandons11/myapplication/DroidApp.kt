package com.nprmanbrandons11.myapplication

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DroidApp:Application() {
    companion object{

        fun getRetrofitUpload():PhotoService = Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breeds/image/").addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PhotoService::class.java)
    }


}
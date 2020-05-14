package com.sf.gtdng.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> cteateService(serviceClass: Class<T>?): T {
        return retrofit.create(serviceClass)
    }
}
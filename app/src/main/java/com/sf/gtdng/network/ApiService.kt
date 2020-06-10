package com.sf.gtdng.network

import com.sf.gtdng.helper.AppLog
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiService {
    fun getRetrofitService(): Retrofit {
        val builder = OkHttpClient.Builder()
        if (AppLog.ENABLE_LOG) {
            val logInterceptor = HttpLoggingInterceptor()
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logInterceptor)
        }
        return Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .client(builder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
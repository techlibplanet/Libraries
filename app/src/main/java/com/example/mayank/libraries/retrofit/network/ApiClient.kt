package com.example.mayank.libraries.retrofit.network

import com.example.mayank.libraries.Constants.API_END_POINT
import com.example.mayank.libraries.Constants.CONNECTION_TIMEOUT
import com.example.mayank.libraries.Constants.READ_TIMEOUT
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    @PublishedApi
    internal var retrofit: Retrofit


    init {
        val gson = GsonBuilder()
                .setLenient()
                .create()

        val httpClient = OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .build()

        retrofit = Retrofit.Builder()
                .baseUrl(API_END_POINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build()
    }

    inline fun <reified T> getService(): T {
        return retrofit.create(T::class.java)
    }


}
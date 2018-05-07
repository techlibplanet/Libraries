package com.example.mayank.libraries

import android.app.Application
import com.example.mayank.libraries.retrofit.network.ApiClient
import retrofit2.Retrofit

class LibrariesApplication : Application(){

    private var mRetrofit: Retrofit? = null

    override fun onCreate() {
        super.onCreate()
    }


    fun getRetrofit(): Retrofit {
        return this.mRetrofit!!
    }


    fun setRetrofit() {
        mRetrofit = ApiClient.buildRetrofit()
    }

}
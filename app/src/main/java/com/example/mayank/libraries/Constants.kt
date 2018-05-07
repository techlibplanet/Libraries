package com.example.mayank.libraries

import android.util.Log

/**
 * Created by Mayank on 4/25/2018.
 */
object Constants {


    val API_END_POINT = "http://www.alchemyeducation.org/"

    val CONNECTION_TIMEOUT: Long = 60
    val READ_TIMEOUT: Long = 60

    fun showLogDebug(tag: String, message: String){
        Log.d(tag, message)
    }
}
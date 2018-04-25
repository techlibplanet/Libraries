package com.example.mayank.libraries

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mayank.libraries.Constants.showLogDebug
import com.example.mayank.libraries.lottie.LottieActivity

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun lottieLibrary(view: View){
        showLogDebug(TAG, "Lottie Library Button Clicked")
        val intent = Intent(this, LottieActivity::class.java)
        startActivity(intent)
    }
}

package com.example.mayank.libraries.widgets

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mayank.libraries.Constants.showLogDebug
import com.example.mayank.libraries.R
import kotlinx.android.synthetic.main.activity_progress_example.*

class ProgressExample : AppCompatActivity() {

    private val TAG = ProgressExample::class.java.simpleName
//    private val MAX_VALUE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_example)

        start_progress_bar.setOnClickListener {
            showLogDebug(TAG, "Progress start button clicked")
            Thread(Runnable {
                this@ProgressExample.runOnUiThread(java.lang.Runnable {
                    progressBar.visibility = View.VISIBLE
                })

//                try {
//                    var i=0;
//                    while(i<Int.MAX_VALUE){
//                        i++
//                    }
//                } catch (e: InterruptedException) {
//                    e.printStackTrace()
//                }
//
//                // when the task is completed, make progressBar gone
//                this@ProgressExample.runOnUiThread(java.lang.Runnable {
//                    progressBar.visibility = View.GONE
//                })
            }).start()

        }


        stop_progress_bar.setOnClickListener{
            if (progressBar!=null){
                this@ProgressExample.runOnUiThread(java.lang.Runnable {
                    progressBar.visibility = android.view.View.GONE
                })
            }
        }
    }
}

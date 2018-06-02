package com.example.mayank.libraries

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mayank.libraries.Constants.showLogDebug
import com.example.mayank.libraries.camera.FinalCameraActivity
import com.example.mayank.libraries.camera.UpdatedCameraActivity
import com.example.mayank.libraries.camerakit.CameraKitActivity
import com.example.mayank.libraries.databinding.DataBindingActivity
import com.example.mayank.libraries.imageCompress.ImageCompressActivity
import com.example.mayank.libraries.lottie.LottieActivity
import com.example.mayank.libraries.magicalcamera.MagicalCamera
import com.example.mayank.libraries.retrofit.NetworkActivity
import com.example.mayank.libraries.tablayout.TabLayoutActivity
import com.example.mayank.libraries.tablayout.example2.TabLayoutExample
import com.example.mayank.libraries.widgets.ProgressExample

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

    fun dataBinding(view: View){
//        startActivity<>();
        val intent = Intent(this, DataBindingActivity::class.java)
        startActivity(intent)
    }

    fun openCameraActivity(view: View){
        showLogDebug(TAG,"Open camera button clicked")
        val intent = Intent(this, FinalCameraActivity::class.java)
        startActivity(intent)
    }

    fun compressActivity(view: View){
        val intent = Intent(this, MagicalCamera::class.java)
        startActivity(intent)
    }

    fun gotoProgress(view: View){
        val intent = Intent(this, ProgressExample::class.java)
        startActivity(intent)
    }

    fun gotoCameraKit(view : View){
        val intent = Intent(this, CameraKitActivity::class.java)
        startActivity(intent)
    }
}

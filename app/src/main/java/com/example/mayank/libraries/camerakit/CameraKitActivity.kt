package com.example.mayank.libraries.camerakit

import android.graphics.Camera
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.mayank.libraries.R
import com.wonderkiln.camerakit.CameraView
import com.wonderkiln.camerakit.CameraKit
import net.rmitsolutions.cameralibrary.MyLibrary


class CameraKitActivity : AppCompatActivity() {

    private val TAG = CameraKitActivity::class.java.simpleName

    private val cameraMethod = CameraKit.Constants.METHOD_STANDARD
    private val cropOutput = false

    private lateinit var camera : CameraView

    val myLibrary = MyLibrary()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_kit)
        camera = findViewById(R.id.camera)
        camera.setMethod(cameraMethod)
        camera.setCropOutput(cropOutput)

        myLibrary.logD(TAG, "Library created")
    }

    override fun onResume() {
        super.onResume()
        camera.start()

    }

    override fun onPause() {
        camera.stop()
        super.onPause()
    }
}

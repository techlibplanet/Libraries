package com.example.mayank.libraries.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.mayank.libraries.R
import kotlinx.android.synthetic.main.activity_camera.*
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore.Images
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import com.example.mayank.libraries.Constants.showLogDebug
import java.io.ByteArrayOutputStream
import android.os.Build
import android.support.annotation.RequiresApi
import com.theartofdev.edmodo.cropper.CropImage
import java.nio.file.Files.size
import android.R.attr.data








class CameraActivity : AppCompatActivity() {

    private val TAG = CameraActivity::class.java.simpleName

    val CAMERA_REQUEST_CODE = 0

    val PIC_CROP = 1
    var bitmap : Bitmap ? = null
    private var picUri : Uri? = null

    private var permissionsRequired = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MEDIA_CONTENT_CONTROL)
    private val PERMISSION_CALLBACK_CONSTANT = 100
    private val REQUEST_PERMISSION_SETTING = 101

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        buttonCapture.setOnClickListener{
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(packageManager) != null){
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            }
        }
        

        buttonCrop.setOnClickListener{


            if (requestPermissions()){
                if (bitmap != null){
                    picUri = getImageUri(this, bitmap!!)
//                    performCrop()
                    CropImage.activity(picUri)
                            .start(this);

                }else{
                    Toast.makeText(this, "Please select an Image", Toast.LENGTH_SHORT).show()
                }
            }


        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
                CAMERA_REQUEST_CODE -> {
                    if (resultCode == Activity.RESULT_OK && data!= null){
                        bitmap = data.extras.get("data") as Bitmap
                        imageViewCamera.setImageBitmap(bitmap)
                    }
                }
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ->{
                    val result = CropImage.getActivityResult(data)
                    if (resultCode === Activity.RESULT_OK) {
                        val resultUri = result.uri
                        showLogDebug(TAG, "Result Uri $resultUri")
//                        imageViewCamera.setImageBitmap(result.bitmap)
                        imageViewCamera.setImageURI(resultUri)
                    } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        val error = result.error
                        showLogDebug(TAG, "Error $error")
                    }
                }
            else ->{
                Toast.makeText(this, "Unrecognized Code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val REQUEST_PERMISSION = 101

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(this, permissionsRequired[0])!= PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, permissionsRequired[2])!= PackageManager.PERMISSION_GRANTED){
            if (shouldShowRequestPermissionRationale(permissionsRequired[0]) && shouldShowRequestPermissionRationale(permissionsRequired[1])
            && shouldShowRequestPermissionRationale(permissionsRequired[2])){

            }
            requestPermissions(permissionsRequired, REQUEST_PERMISSION)
            return false
        }else {
            return true
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_PERMISSION ->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED  ){
                    showLogDebug(TAG, "All Permission Granted")
                }else {
                    requestPermissions()
                }
            }

        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }




}

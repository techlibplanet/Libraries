package com.example.mayank.libraries.ucrop

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.mayank.libraries.Constants.showLogDebug
import com.example.mayank.libraries.R
import com.example.mayank.libraries.imageCompress.FileUtil
import com.frosquivel.magicalcamera.MagicalCamera
import com.frosquivel.magicalcamera.MagicalPermissions
import com.yalantis.ucrop.UCrop
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_magical_camera.*
import java.io.ByteArrayOutputStream
import java.io.File

class UcropActivity : AppCompatActivity() {

    private val TAG = UcropActivity::class.java.simpleName

    private var magicalPermission : MagicalPermissions? = null

    private val RESIZE_PHOTO_PIXELS_PERCENTAGE = 100

    private var magicalCamera : MagicalCamera? = null
    private var bitmap : Bitmap? = null

    private var actualImage : File? = null

    private var sourceUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ucrop)

        val permissions = arrayOf<String>(Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

        magicalPermission = MagicalPermissions(this, permissions)
        val runnable = Runnable {
            //TODO location permissions are granted code here your feature
            Toast.makeText(this, "Thanks for granting location permissions", Toast.LENGTH_LONG).show()
        }
        magicalPermission!!.askPermissions(runnable)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ucrop_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.camera ->{
                showLogDebug(TAG, "Camera Button Clicked")
                magicalCamera = MagicalCamera(this,RESIZE_PHOTO_PIXELS_PERCENTAGE, magicalPermission)
                magicalCamera?.takePhoto()
            }

            R.id.crop ->{
                showLogDebug(TAG, "Crop Selected")
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when(requestCode){
            MagicalCamera.TAKE_PHOTO ->{
                magicalCamera?.resultPhoto(requestCode,resultCode,data)

                sourceUri = getImageUri(this, magicalCamera?.photo!!)
                showLogDebug(TAG,"$sourceUri")
                actualImage = FileUtil.from(this, sourceUri!!)

                bitmap = Compressor(this).compressToBitmap(actualImage)
//        bitmap = magicalCamera?.photo

                if (bitmap!= null){
                    imageViewMagical.setImageBitmap(magicalCamera?.faceDetector(50, Color.GREEN))
                }

            }
            UCrop.REQUEST_CROP  ->{
                sourceUri = data?.data
                if (sourceUri!=null){
                    startCrop(sourceUri!!)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun startCrop(sourceUri: Uri) {
        var destinationFileName = "Image-${System.currentTimeMillis()}.jpg"


        var uCrop = UCrop.of(sourceUri, Uri.fromFile(File(cacheDir, destinationFileName)))

       //================================start From here =========================================
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }
}

package com.example.mayank.libraries.camera

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.mayank.libraries.Constants
import com.example.mayank.libraries.Constants.showLogDebug
import com.example.mayank.libraries.R
import com.example.mayank.libraries.imageCompress.FileUtil
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_camera.*
import org.apache.commons.io.FileUtils
import java.io.*
import java.nio.file.Files.size



class UpdatedCameraActivity : AppCompatActivity() {

    private val TAG = UpdatedCameraActivity::class.java.simpleName
    var picUri : Uri? = null
    var bitmap : Bitmap? = null
    val CAMERA_REQUEST_CODE = 100
    var actualImage : File? = null
    var compressImage : File? = null
    var fname : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_camera_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.camera ->{
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (cameraIntent.resolveActivity(packageManager) != null){
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                }
            }
            R.id.crop ->{
                if (bitmap != null){
                    picUri = getImageUri(this, bitmap!!)
                    CropImage.activity(picUri).start(this);
                }else{
                    Toast.makeText(this, "Please select an Image", Toast.LENGTH_SHORT).show()
                }

            }
            R.id.save ->{
                if (bitmap != null){
//                    val uri = getImageUri(this, bitmap!!)
//                    actualImage = FileUtil.from(this, uri)
//                    bitmap = Compressor(this).compressToBitmap(actualImage)
////                    saveFile(compressImage!!)
//                    saveFileToInternalStorage(bitmap!!) // working
//                    saveToInternalStorage(bitmap!!)
                    saveFileByteArray(bitmap!!)
//
                }else {
                    Toast.makeText(this, "Please select an Image", Toast.LENGTH_SHORT).show()
                }
            }
            else ->{
                Toast.makeText(this, "Unrecognized Menu Option", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveFileByteArray(bitmap: Bitmap) {
        showLogDebug(TAG, "Inside save file byte array")
        val byteArrayStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayStream)
        val byteArray = byteArrayStream.toByteArray()
        showLogDebug(TAG, "Byte Array : $byteArray")
    }

    // To save File
    private fun saveFile(file: File){
        val timeInMillis = System.currentTimeMillis()
        val fileName = "File-$timeInMillis"
        val fileContent = file.readBytes()
        try {
            applicationContext.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                it.write(fileContent)
                showLogDebug(TAG, "File saved successfully")
            }
        }catch (e: Exception){
            showLogDebug(TAG, "Error - $e")
        }
    }


    private fun saveFileToInternalStorage(bitmap: Bitmap){
        val cw = ContextWrapper(this)
        val directory = cw.getDir("images", Context.MODE_PRIVATE)
        showLogDebug(TAG, "Folder Location - $directory")
        val timeInMillis = System.currentTimeMillis()
        fname = "Image-$timeInMillis.jpg"
        showLogDebug(TAG, "File Name - $fname")
        val file = File(directory, fname)
        if (file.exists()){
            file.delete()
        }
        var fos : FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100, fos)
            showLogDebug(TAG, "Image store successfully")
            Toast.makeText(this, "Image store successfully", Toast.LENGTH_SHORT).show()
        }catch (e: Exception){
            showLogDebug(TAG, "Error - $e")
        }finally {
            try {
                fos!!.flush()
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
                showLogDebug(TAG, "Error - $e")
            }
        }

    }

    // Save to Internal Storage
//    private fun saveToInternalStorage(bitmapImage: Bitmap): String {
//        val cw = ContextWrapper(applicationContext)
//        // path to /data/data/yourapp/app_data/images
//        val directory = cw.getDir("images", Context.MODE_PRIVATE)
//        // Create imageDir
//        Constants.showLogDebug(TAG, "Folder Location - $directory")
//
//        val timeInMillis = System.currentTimeMillis()
//
//        fname = "Image-$timeInMillis.jpg"
//
//        Constants.showLogDebug(TAG, "File Name $fname")
//        val file = File(directory, fname)
//        if (file.exists()){
//            Constants.showLogDebug(TAG, "File already Exist")
//            file.delete()
//            fname = null
//        }
//        var fos: FileOutputStream? = null
//        try {
//            fos = FileOutputStream(file)
//
//            // Use the compress method on the BitMap object to write image to the OutputStream
//            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos)
//
//
//
//            Constants.showLogDebug(TAG, "Image saved successfully")
//            Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Constants.showLogDebug(TAG, "Failed to store Image")
//        } finally {
//            try {
//                fos!!.flush()
//                fos.close()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//        return directory.absolutePath
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CAMERA_REQUEST_CODE ->{
                if (resultCode == Activity.RESULT_OK && data != null){
                    bitmap = data.extras.get("data") as Bitmap
                    imageViewCamera.setImageBitmap(bitmap)
                }
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ->{
                val result = CropImage.getActivityResult(data)
                if (resultCode === Activity.RESULT_OK) {
                    val resultUri = result.uri
                    Constants.showLogDebug(TAG, "Result Uri $resultUri")
                    imageViewCamera.setImageURI(resultUri)
                    val drawable = imageViewCamera.drawable as BitmapDrawable
                    bitmap = drawable.bitmap
                } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.error
                    Constants.showLogDebug(TAG, "Error $error")
                }
            }
        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }
}
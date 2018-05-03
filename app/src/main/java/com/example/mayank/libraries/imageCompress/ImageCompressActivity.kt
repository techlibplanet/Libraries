package com.example.mayank.libraries.imageCompress

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.graphics.BitmapCompat
import android.view.View
import com.example.mayank.libraries.R
import kotlinx.android.synthetic.main.activity_image_compress.*
import android.widget.Toast
import com.example.mayank.libraries.Constants.showLogDebug
import id.zelory.compressor.Compressor
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.DecimalFormat
import java.util.*


class ImageCompressActivity : AppCompatActivity() {

    private val TAG = ImageCompressActivity::class.java.simpleName

    val CAMERA_REQUEST_CODE = 0
    private var bitmap : Bitmap? = null
    var compressImage : File? = null
    var actualImage : File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_compress)
    }


    fun captureImage(view: View){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val cw = ContextWrapper(applicationContext)
        // path to /data/data/yourapp/app_data/imageDir
        val directory = cw.getDir("images", Context.MODE_PRIVATE)
        if (cameraIntent.resolveActivity(packageManager) != null){
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        }
    }

    fun compressImage(view: View){
        if (bitmap == null){
            showError("Please select an Image!")
        } else{
            try {
                compressImage = Compressor(this).compressToFile(actualImage)
                compressImageView.setImageBitmap(bitmap)
                showLogDebug(TAG, "Size : ${getReadableFileSize(compressImage?.length()!!)}")

            }catch (e : Exception){
                e.printStackTrace()
                showLogDebug("Error", e.toString())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CAMERA_REQUEST_CODE ->{
                if (resultCode == Activity.RESULT_OK && data != null){
//                    bitmap = data.extras.get("data") as Bitmap

                    showLogDebug(TAG, "Data - $data")

                    bitmap = data.extras.get("data") as Bitmap
                    val byteCount = BitmapCompat.getAllocationByteCount(bitmap!!)
                    showLogDebug(TAG, "Bitmap size - ${byteCount/1024}")
                    val uri = getImageUri(this, bitmap!!)


                    actualImage = FileUtil.from(this, uri)
                    actualImageView.setImageBitmap(BitmapFactory.decodeFile(actualImage?.absolutePath))
                    showLogDebug(TAG, "Actual Image Size ; ${getReadableFileSize(actualImage?.length()!!)}")
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

    fun convertBitmap(bitmap: Bitmap){
        val byteArrayOutputStream = ByteArrayOutputStream()

    }

    private fun getRandomColor(): Int {
        val rand = Random()
        return Color.argb(100, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256))
    }


    fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun getReadableFileSize(size: Long): String {
        if (size <= 0) {
            return "0"
        }
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
    }
}

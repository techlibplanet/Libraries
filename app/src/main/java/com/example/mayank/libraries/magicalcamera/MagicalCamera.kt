package com.example.mayank.libraries.magicalcamera

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.mayank.libraries.R
import com.frosquivel.magicalcamera.MagicalPermissions
import android.Manifest.permission
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.widget.Toast
import android.R.attr.keySet
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.mayank.libraries.Constants.showLogDebug
import com.example.mayank.libraries.imageCompress.FileUtil
import com.frosquivel.magicalcamera.MagicalCamera

//and maybe you need in some ocations
import com.frosquivel.magicalcamera.Objects.MagicalCameraObject
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.activity_magical_camera.*
import net.rmitsolutions.cameralibrary.MyLibrary
import net.rmitsolutions.cameralibrary.MyPermissions
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class MagicalCamera : AppCompatActivity() {

    private val TAG = com.example.mayank.libraries.magicalcamera.MagicalCamera::class.java.simpleName

    private lateinit var magicalPermissions: MagicalPermissions
    private lateinit var myPermissions : MyPermissions

    private val RESIZE_PHOTO_PIXELS_PERCENTAGE = 100

    private var magicalCamera : MagicalCamera? = null
    private var bitmap : Bitmap? = null

    private var picUri : Uri? = null

    var actualImage: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_magical_camera)

        val permissions = arrayOf<String>(Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)

        magicalPermissions = MagicalPermissions(this, permissions)

        myPermissions = MyPermissions(this, permissions)
        val runnable = Runnable {
            //TODO location permissions are granted code here your feature
            Toast.makeText(this, "Thanks for granting location permissions", Toast.LENGTH_LONG).show()
        }
        magicalPermissions.askPermissions(runnable)
        myPermissions.askPermissions(runnable)


        // To load Image from storage
//        val root = Environment.getExternalStorageDirectory().toString()
//        val dir = "$root/Pictures/Images"
//        showLogDebug(TAG, "External path $dir")
//        loadImageFromStorage(dir,"Image-1525929422319_20180510104702.jpeg")

    }


    fun magical(view: View){
        magicalCamera = MagicalCamera(this,RESIZE_PHOTO_PIXELS_PERCENTAGE, magicalPermissions)
        magicalCamera?.takePhoto()

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val map = magicalPermissions.permissionResult(requestCode, permissions, grantResults)
        for (permission in map?.keys!!) {
            showLogDebug("PERMISSIONS", permission + " was: " + map[permission])
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            MagicalCamera.TAKE_PHOTO ->{
                magicalCamera?.resultPhoto(requestCode,resultCode,data)
                val uri = getImageUri(this, magicalCamera?.photo!!)
                showLogDebug(TAG,"$uri")
                actualImage = FileUtil.from(this, uri)
                bitmap = Compressor(this).compressToBitmap(actualImage)
                if (bitmap!= null){
                    imageViewMagical.setImageBitmap(magicalCamera?.faceDetector(50, Color.GREEN))
                }
                getBitmapInformation(magicalCamera!!)
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ->{
                val result = CropImage.getActivityResult(data)
                if (resultCode === Activity.RESULT_OK) {
                    val resultUri = result.uri
                    showLogDebug(TAG, "Result Uri $resultUri")
                    imageViewMagical.setImageURI(resultUri)
                    val drawable = imageViewMagical.drawable as BitmapDrawable
                    bitmap = drawable.bitmap
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_camera_activity,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.camera ->{
                magicalCamera = MagicalCamera(this,RESIZE_PHOTO_PIXELS_PERCENTAGE, magicalPermissions)
                magicalCamera?.takePhoto()
            }
            R.id.crop ->{
                if (bitmap != null){
                    picUri = getImageUri(this, bitmap!!)
//                    performCrop()
                    CropImage.activity(picUri).start(this);
                }else{
                    Toast.makeText(this, "Please select an Image", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.save ->{
                if (bitmap!= null){
                    val path = magicalCamera?.savePhotoInMemoryDevice(bitmap, "Image-${System.currentTimeMillis()}", "Images",MagicalCamera.JPEG, true)

                    if(path != null){
                        showLogDebug(TAG, "Path - $path")
                        Toast.makeText(this, "The photo is save in device, please check this path: $path", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Sorry your photo dont write in devide, please contact with fabian7593@gmail and say this error", Toast.LENGTH_SHORT).show()
                    }
                }else {
                    Toast.makeText(this, "Please capture a Picture", Toast.LENGTH_SHORT).show()
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getImageUri(context: Context, bitmap: Bitmap): Uri {
//        val bytes = ByteArrayOutputStream()
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    // To load image from storage
    private fun loadImageFromStorage(path: String, fileName : String){
        try {
            val f = File(path, fileName)
            val b = BitmapFactory.decodeStream(FileInputStream(f))
            imageViewMagical.setImageBitmap(b)
        }catch (e: FileNotFoundException){
            e.printStackTrace()
            showLogDebug(TAG, "Error $e")
        }

    }

    private fun getBitmapInformation(magicalCamera: MagicalCamera){
        showLogDebug(TAG, "Inside get bitmap information")
        val buildInfo = StringBuilder()
        if (magicalCamera.photo != null){
            if (magicalCamera.initImageInformation()){
                if (notNullNotFill("${magicalCamera.privateInformation.latitude}" )){
                    buildInfo.append("Latitude : ${magicalCamera.privateInformation.latitude} \n")
                }
                if (notNullNotFill("${magicalCamera.privateInformation.latitudeReference}")){
                    buildInfo.append("Latitude Reference : ${magicalCamera.privateInformation.latitudeReference} \n")
                }

                if (notNullNotFill("${magicalCamera.privateInformation.longitude}")){
                    buildInfo.append("Longitude : ${magicalCamera.privateInformation.longitude} \n")
                }
                if (notNullNotFill("${magicalCamera.privateInformation.latitudeReference}")){
                    buildInfo.append("Longitude Reference : ${magicalCamera.privateInformation.longitudeReference} \n")
                }
                if (notNullNotFill("${magicalCamera.privateInformation.dateTimeTakePhoto}")){
                    buildInfo.append("Date time take photo : ${magicalCamera.privateInformation.dateTimeTakePhoto} \n")
                }
                if (notNullNotFill("${magicalCamera.privateInformation.dateStamp}")){
                    buildInfo.append("Date Stamp : ${magicalCamera.privateInformation.dateStamp} \n")
                }
                if (notNullNotFill("${magicalCamera.privateInformation.orientation}")){
                    buildInfo.append("Orientation : ${magicalCamera.privateInformation.orientation} \n")
                }
                if (notNullNotFill("${magicalCamera.privateInformation.imageLength}")){
                    buildInfo.append("Image Length : ${magicalCamera.privateInformation.imageLength} \n")
                }
                if (notNullNotFill("${magicalCamera.privateInformation.imageWidth}")){
                    buildInfo.append("Image Width : ${magicalCamera.privateInformation.imageWidth} \n")
                }
                if (notNullNotFill("${magicalCamera.privateInformation.modelDevice}")){
                    buildInfo.append("Model device : ${magicalCamera.privateInformation.modelDevice} \n")
                }
                if (notNullNotFill("${magicalCamera.privateInformation.makeCompany}")){
                    buildInfo.append("Make Company : ${magicalCamera.privateInformation.makeCompany}")
                }
                showLogDebug(TAG, buildInfo.toString())
            }else {
                Toast.makeText(this, "Photo doesn't have any information!", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "Image is null", Toast.LENGTH_SHORT).show()
        }

    }

    private fun notNullNotFill(validate: String?): Boolean {
        return if (validate != null) {
            validate.trim { it <= ' ' } != ""
        } else {
            false
        }
    }
}

package com.example.mayank.libraries.camera

import android.app.Activity
import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.mayank.libraries.Constants.showLogDebug
import com.example.mayank.libraries.R
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_final_camera.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class FinalCameraActivity : AppCompatActivity() {

    private val TAG = FinalCameraActivity::class.java.simpleName

    val CAMERA_REQUEST_CODE = 100
    private var bitmap : Bitmap? = null
    private var picUri : Uri? = null
    var cropResult : CropImageView.CropResult? = null
    var actualImage : File? = null
    var compressedImage : File? = null
    var pictureImagePath : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_camera)

        val sharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE)
        val imageString = sharedPreferences.getString("bytearray", "null")
        if (imageString!="null"){
            val imageArray = Base64.decode(imageString, Base64.DEFAULT)
            val bitmapData = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.size)
            if (bitmapData!= null){
                showLogDebug(TAG, "Bitmap data is not null")
                cropImageView.setImageBitmap(bitmapData)
            }else {
                showLogDebug(TAG, "Bitmap data is null")
            }

        }
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
                    try {
                        actualImage = createImageFile()
//                        val timeStamp = System.currentTimeMillis()
//                        val imageFileName = "Image_$timeStamp"
//                        actualImage = File(Environment.getExternalStorageDirectory(), imageFileName)
                    }catch (e : IOException){
                        showLogDebug(TAG, "Error : $e")
                    }
                    if (actualImage!=null){
//                        picUri = Uri.fromFile(actualImage)
                        picUri = FileProvider.getUriForFile(this,"com.example.mayank.libraries.provider", actualImage!!)
                        showLogDebug(TAG, "Pic Uri : $picUri")
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri)
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                    }
                }
//                startActivityForResult(pickImageChooserIntent, CAMERA_REQUEST_CODE)
                return true
            }
            R.id.crop ->{
                if (bitmap!=null){
                    picUri = getImageUri(this, bitmap!!)
                    CropImage.activity(picUri).start(this);
                }else {
                    Toast.makeText(this, "Please select an Image", Toast.LENGTH_SHORT).show()
                }
                return true
            }
            R.id.save -> {
                if (bitmap != null){
                    saveFileByteArray(bitmap!!)
                }else {
                    Toast.makeText(this, "Please select an Image", Toast.LENGTH_SHORT).show()
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when(requestCode){
            CAMERA_REQUEST_CODE ->{
                if (resultCode==Activity.RESULT_OK){
//                    val imageUri = getPickImageResultUri(data)
//                    showLogDebug(TAG, "Camera Uri : $imageUri")
//                    bitmap = getImageBitmap(this, imageUri!!)
//                    cropImageView.setImageBitmap(bitmap)
                    compressedImage = File(mCurrentPhotoPath)
                    if (compressedImage?.exists()!!){
                        showLogDebug(TAG, "Image found successfully")
                        bitmap = BitmapFactory.decodeFile(compressedImage?.absolutePath)
                        getBitmapDetails()
                        cropImageView.setImageBitmap(bitmap)
                    }else{
                        showLogDebug(TAG, "No Image Found")
                    }


                }
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK){
                    val resultUri = result.uri
                    showLogDebug(TAG, "Result Uri - $resultUri")
                    bitmap = getImageBitmap(this, resultUri)
                    cropImageView.setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun getBitmapDetails() {
        val file = File(mCurrentPhotoPath)
        if (file.exists()){
            val imageInfo = ImageInformation()
            val data = imageInfo.getImageInformation(mCurrentPhotoPath!!)
            showLogDebug(TAG, "Latitude : ${data?.latitude}")
            showLogDebug(TAG, "Longitude : ${data?.longitude}")
            showLogDebug(TAG, "Date Time Take Photo : ${data?.dateTimeTakePhoto}")
            showLogDebug(TAG, "Image Length : ${data?.imageLength}")
            showLogDebug(TAG, "Imaeg Width : ${data?.imageWidth}")
            showLogDebug(TAG, "Model : ${data?.modelDevice}")
            showLogDebug(TAG, "Maker : ${data?.makeCompany}")
        }
    }

    var mCurrentPhotoPath: String? = null

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = System.currentTimeMillis()
        val imageFileName = "Image_$timeStamp"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
//        val image = File(Environment.getExternalStorageDirectory(), imageFileName)

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun saveToShared(byteArray: ByteArray) {
        val sharedPreference = getSharedPreferences("shared", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        val base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)
        editor.putString("bytearray", base64String)
        editor.commit()
    }

    private fun saveFileByteArray(bitmap: Bitmap) {
        showLogDebug(TAG, "Inside save file byte array")
        val byteArrayStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayStream)
        val byteArray = byteArrayStream.toByteArray()
        showLogDebug(TAG, "Byte Array : $byteArray")
        saveToShared(byteArray)

        // To show bitmap
//        val bitmapData = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//        if (bitmapData!= null){
//            showLogDebug(TAG, "Bitmap data is not null")
//        }else {
//            showLogDebug(TAG, "Bitmap data is null")
//        }
    }


    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
//        val bytes = ByteArrayOutputStream()
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun getImageBitmap(context: Context, uri: Uri): Bitmap? {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        return bitmap
    }

//    val pickImageChooserIntent: Intent
//        get() {
//            val outputFileUri = captureImageOutputUri
//
//            val allIntents = ArrayList<Intent>()
//            val packageManager = packageManager
//            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            val listCam = packageManager.queryIntentActivities(captureIntent, 0)
//            for (res in listCam) {
//                val intent = Intent(captureIntent)
//                intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
//                intent.`package` = res.activityInfo.packageName
//                if (outputFileUri != null) {
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
//                }
//                allIntents.add(intent)
//            }
//            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
//            galleryIntent.type = "image/*"
//            val listGallery = packageManager.queryIntentActivities(galleryIntent, 0)
//            for (res in listGallery) {
//                val intent = Intent(galleryIntent)
//                intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
//                intent.`package` = res.activityInfo.packageName
//                allIntents.add(intent)
//            }
//            var mainIntent = allIntents[allIntents.size - 1]
//            for (intent in allIntents) {
//                if (intent.component!!.className == "com.android.documentsui.DocumentsActivity") {
//                    mainIntent = intent
//                    break
//                }
//            }
//            allIntents.remove(mainIntent)
//            val chooserIntent = Intent.createChooser(mainIntent, "Select source")
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toTypedArray<Parcelable>())
//
//            return chooserIntent
//        }

    private val captureImageOutputUri: Uri?
        get() {
            var outputFileUri: Uri? = null
            val getImage = externalCacheDir
            if (getImage != null) {
                outputFileUri = Uri.fromFile(File(getImage.path, "pickImageResult.jpeg"))
            }
            return outputFileUri
        }







}

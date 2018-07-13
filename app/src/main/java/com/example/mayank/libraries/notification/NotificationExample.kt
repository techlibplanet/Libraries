package com.example.mayank.libraries.notification

import android.app.Notification
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mayank.libraries.R
import net.rmitsolutions.cameralibrary.Constants.logD
import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat


class NotificationExample : AppCompatActivity() {

    private val TAG = NotificationExample::class.java.simpleName
    private val NOTIFICATION_CHANNEL_ID = "default"
    private var notifyId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_example)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkNotification(view: View){
        logD(TAG, "Check notification button clicked")
//        NotificationHelper.notify(this, "Notification", "Default")

        val message = ArrayList<String>()
        message.add("This is message one.")
        message.add("This is message two.")
        message.add("This is message three.")
        NotificationHelper.newNotifyGroupedError(this, "MyApp", "Some error occurred !",message)

//        var notificationChannel : NotificationChannel? = null
//        //Notification channel should only be created for devices running Android 26
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "channel_name", NotificationManager.IMPORTANCE_DEFAULT)
//
//            //Boolean value to set if lights are enabled for Notifications from this Channel
//            notificationChannel.enableLights(true)
//
//            //Boolean value to set if vibration is enabled for Notifications from this Channel
//            notificationChannel.enableVibration(true)
//
//            //Sets the color of Notification Light
//            notificationChannel.lightColor = Color.GREEN
//
//            //Set the vibration pattern for notifications. Pattern is in milliseconds with the format {delay,play,sleep,play,sleep...}
//            notificationChannel.vibrationPattern = longArrayOf(500, 500, 500, 500, 500)
//
//            //Sets whether notifications from these Channel should be visible on Lockscreen or not
//            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
//        }
//
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(notificationChannel)
//
//        //We pass the unique channel id as the second parameter in the constructor
//        val notificationCompatBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
//
////Title for your notification
//        notificationCompatBuilder.setContentTitle("This is title")
//
////Subtext for your notification
//        notificationCompatBuilder.setContentText("This is subtext")
//
////Small Icon for your notificatiom
//        notificationCompatBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
//
////Large Icon for your notification
////        notificationCompatBuilder.setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round))
//
//        notificationManager.notify(++notifyId, notificationCompatBuilder.build())
    }
}

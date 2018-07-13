package com.example.mayank.libraries.notification

import android.app.*
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.example.mayank.libraries.R
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.v4.content.ContextCompat
import java.util.ArrayList


object NotificationHelper {

    private val NOTIFICATION_CHANNEL_ID = "default"
    private val NOTIFICATION_CHANNEL_NAME = "MfExpert"
    private var notifyId = 0
    private lateinit var notificationManager : NotificationManager

    private fun createNotificationChannel(context: Context){
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = NOTIFICATION_CHANNEL_NAME
//            val description = "Sample description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
//            channel.description = description
            channel.lightColor = Color.CYAN;
            channel.canShowBadge();
            channel.setShowBadge(true);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    @Synchronized
    fun notify(context: Context, subText: String, notificationCategory: String) {
        createNotificationChannel(context)
        // Create an explicit intent for an Activity in your app
        val intent = Intent(context, NotificationExample::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val mBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("MfExpert")
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentText("This is a sample notification !!!")
                .setBadgeIconType(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

                // Set the intent that will fire when the user taps the notification
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_round))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
        notificationManager.notify(++notifyId, mBuilder);
    }


    @Synchronized
    fun newNotifyGroupedError(context: Context, subText: String, summaryText: String, messages: ArrayList<String>){
        createNotificationChannel(context)

        val intent = Intent(context, NotificationExample::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)


        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(context.getString(R.string.app_name))
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentText(subText)
                .setBadgeIconType(R.mipmap.ic_launcher)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setCategory(Notification.CATEGORY_ERROR)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

                // Set the intent that will fire when the user taps the notification
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_round))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        val inboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.setBigContentTitle(subText)
                .setSummaryText(summaryText)

        for (message in messages) {
            inboxStyle.addLine(message)
        }

        builder.setStyle(inboxStyle)
                .setGroupSummary(true)
                .setGroup("AppAccountName")
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(++notifyId, builder.build())
    }

}
package com.narendar.letstravel.serviceprovider

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.narendar.letstravel.R

class MyFirebaseMessegingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body
        val extraData= remoteMessage.data as HashMap<String, String>
        val user_name=extraData.get("user_name") as String
        val service=extraData.get("service") as String
        val pn_num=extraData.get("pn_num") as String
        val builder = NotificationCompat.Builder(this@MyFirebaseMessegingService, "0")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notificationbell)
        val intentnoti: Intent
        if(service.equals("car_breakdown")){
            intentnoti= Intent(this,ReceiveNotificationActivity::class.java)

        }else{
            intentnoti = Intent(this, ReceiveNotificationActivity::class.java)
        }
        intentnoti.putExtra("user_name_noti",user_name)
        intentnoti.putExtra("service_noti",service)
        intentnoti.putExtra("pn_num",pn_num)
        val pendingIntent = PendingIntent.getActivity(
            this@MyFirebaseMessegingService,
            10,
            intentnoti,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.setContentIntent(pendingIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val id = System.currentTimeMillis().toInt()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel("0", "Notification", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(id, builder.build())
    }
}
package com.sam.healthsense.fcm

//import android.Manifest
//import android.app.Notification
//import android.content.Context
//import android.content.pm.PackageManager
//import android.util.Log
//import androidx.core.app.ActivityCompat
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage
//import com.sam.healthsense.R
//import com.sam.healthsense.Utils.Constants
//import kotlin.random.Random
//
//class MessagingService : FirebaseMessagingService() {
//    override fun onCreate() {
//        super.onCreate()
//        Log.d("TAG", "${this::class.simpleName} created")
//    }
//
//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//        Log.d("TAG", "onNewToken: $token")
//        // Sample token - dkCQco7YQ8Ci_sX8AD6VsY:APA91bFk5vPbtIzs25ZNIzkOhGGQr3xYnW06DLagPpuTMpav5vGag3B55Sfu678dvvt133fInEbYIkbeJ10KfYSLQW6u9d-lt0G2YfPUD-cLH8HcKspl0bY
//    }
//
//    override fun onMessageReceived(message: RemoteMessage) {
//        super.onMessageReceived(message)
//        val title = message.notification?.title
//        val body = message.notification?.body
//
//        Log.d("TAG", "onMessageReceived: $title $body")
//
//
//        showNotification(title ?: "Default Title", body ?: "Default Body")
//    }
//
//    private fun Context.showNotification(title: String, body: String) {
//        val notification: Notification =
//            NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentTitle(title)
//                .setContentText(body)
//                .build()
//
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            Log.d("TAG", "Notification permission is not available.")
//            return
//        }
//
//        Log.d("TAG", "Showing notification")
//
//        NotificationManagerCompat
//            .from(this)
//            .notify(Random.nextInt(), notification)
//    }
//}

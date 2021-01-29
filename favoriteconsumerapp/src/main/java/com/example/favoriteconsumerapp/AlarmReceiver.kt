package com.example.favoriteconsumerapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val NOTIFICATION_REQUEST_CODE = 200
        const val TYPE_REPEATING = "Github App"
        const val EXTRA_MESSAGE = "message"

        // Siapkan 2 id untuk 2 macam alarm, onetime dan repeating
        private const val ID_REPEATING = 101

    }

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        val title = TYPE_REPEATING
        val notifId = ID_REPEATING

        message?.let { showAlarmNotification(context, title, it, notifId) }
    }

    // metode untuk menampilkan notifikasi
    private fun showAlarmNotification(context: Context, title: String, message: String, notifId: Int) {

        val channelId = "Channel_1"
        val channelName = "AlarmManager channel"

        // Tambahkan obyek PendingIntent sebagai action ketika notif ditekan
        val intent = Intent(context, FavoriteUserActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(context,
            NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notifications_active)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        /*
        Untuk android Oreo ke atas perlu menambahkan notification channel
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            val channel = NotificationChannel(channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(channelId)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()

        notificationManagerCompat.notify(notifId, notification)

    }

}

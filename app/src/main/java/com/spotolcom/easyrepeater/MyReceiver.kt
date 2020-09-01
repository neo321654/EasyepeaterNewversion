package com.spotolcom.easyrepeater

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startActivity


class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
       Log.d("mytag", "16;onReceive: Onreciev "+intent.getStringExtra("prase"))

        creatNotif(context,intent.getStringExtra("prase"))
        //startActivity(Intent(context, MyAppMainActivity::class.java))
    }

    private fun creatNotif(context: Context, phrase: String?){

        var builder = NotificationCompat.Builder(context, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
             // .setContentTitle(".")
            .setContentText(phrase)
            .setPriority(NotificationCompat.PRIORITY_MAX)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(111, builder.build())
        }}
}
package com.spotolcom.easyrepeater

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.spotolcom.easyrepeater.ui.home.HomeFragment
import com.spotolcom.easyrepeater.ui.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class ForegroundService : Service() {

    private val CHANNEL_ID = "ForegroundService Kotlin"
    companion object {
        fun startService(context: Context, message: String) {
            val startIntent = Intent(context, ForegroundService::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }
        fun stopService(context: Context) {
            val stopIntent = Intent(context, ForegroundService::class.java)
            context.stopService(stopIntent)
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //do heavy work on a background thread
        var act = intent?.action
        when (act) {
            "ACTION_PLAY" -> startTraining()
            "ACTION_STOP" -> stopTraining()
            else -> {

                val input = intent?.getStringExtra("inputExtra")
                createNotificationChannel()
                val notificationIntent = Intent(this, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(
                    this,
                    0, notificationIntent, 0
                )
                // Add Play button intent in notification.
                val playIntent = Intent(this, ForegroundService::class.java)
                playIntent.action = "ACTION_PLAY"
                val pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0)
                val playAction =
                    NotificationCompat.Action(
                        android.R.drawable.ic_media_play,
                        "Play",
                        pendingPlayIntent
                    )

                val playIntent1 = Intent(this, ForegroundService::class.java)
                playIntent1.action = "ACTION_STOP"
                val pendingPlayIntent1 = PendingIntent.getService(this, 0, playIntent1, 0)
                val playAction1 =
                    NotificationCompat.Action(
                        android.R.drawable.ic_media_pause,
                        "Stop",
                        pendingPlayIntent1
                    )


                val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Foreground Service Kotlin Example")
                    .setContentText(input)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(pendingIntent)
                    .addAction(playAction)
                    .addAction(playAction1)
                    .build()
                startForeground(1, notification)

            }
        }
        return START_NOT_STICKY
    }




    private fun startTraining() {
        runBlocking {
             var wordsToAlarm: List<Word>? =null
            val job = launch(this.coroutineContext) { //(2)
                wordsToAlarm  = getListFromBase() //(3)

            }
            job.join() //(4)

            Log.d("mytag", "104;startTraining: $wordsToAlarm")
            wordsToAlarm?.let { addAlarms(it) }




        }
    }

      private suspend fun getListFromBase(): List<Word> {
         val db = Room.databaseBuilder(
             applicationContext,
             WordRoomDatabase::class.java, "word_database"
         ).build()

         var words: List<Word> = db.wordDao().getRandWords()
//         Log.d("mytag", "94;startTraining: $words")
         return words
     }
      fun addAlarms(listData:List<Word> ) {
        val list_data = mutableListOf("one", "two", "three", "four")

        val am: AlarmManager
        am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time2 = 1L//число минут между повторами
        var count = 3//число повторов
        val time1: Long = 1000 * 60 * time2
        var time = 1000 * 60.toLong()

        if (count > list_data.size) count = list_data.size

        for (x in 0 until count) {
            val phrase: String = list_data[x]

            val phraseIntent = Intent(this, ForegroundService::class.java)
            phraseIntent.putExtra("prase", phrase)
            phraseIntent.action = "ACTION_PHRASE"
            val pendingPhraseIntent = PendingIntent.getService(this, x, phraseIntent, 0)

    //            var pIntent1: PendingIntent?
    //
    //            pIntent1 = PendingIntent.getBroadcast(this, x, pendingPhraseIntent, 0)

            am[AlarmManager.RTC, System.currentTimeMillis() + time] = pendingPhraseIntent


    //                        if(x == count){
    //                            Toast.makeText(this, "count = x", Toast.LENGTH_SHORT).show();
    //                            stopForegroundService();
    //                        }
            time = time + time1
        }
    }

    private fun stopTraining() {

        val am1 = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        for (i in 0 until 30) {
            var intent_prase_band1: Intent
            var pIntent_main: PendingIntent?
            intent_prase_band1 = Intent("com.uthink.ring.ACTION_INCOMING_RINGING")
            pIntent_main = PendingIntent.getBroadcast(this, i,  intent_prase_band1,PendingIntent.FLAG_NO_CREATE
            )
            if (pIntent_main != null) {
                am1.cancel(pIntent_main)
                pIntent_main.cancel()
            }
        }
        stopSelf();
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }
    fun creatNotif(context: Context,phrase:String){

        var builder = NotificationCompat.Builder(context, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("textTitle")
            .setContentText(phrase)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(111, builder.build())
        }}
}

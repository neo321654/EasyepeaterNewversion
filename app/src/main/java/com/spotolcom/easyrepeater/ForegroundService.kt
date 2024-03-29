package com.spotolcom.easyrepeater

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.spotolcom.easyrepeater.data.Word
import com.spotolcom.easyrepeater.data.WordRoomDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
            "ACTION_STOP" -> {
                stopTraining()
             //   creatNotif(this, "llll")
            }
            "Play_act" -> {
                startTraining()
            }
            "ACTION_PHRASE" -> {

            }

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
                playIntent.action = "Play_act"
                val pendingPlayIntent = PendingIntent.getService(this, 12221, playIntent, 0)
                val playAction =
                    NotificationCompat.Action(
                        android.R.drawable.ic_media_play,
                        "Play",
                        pendingPlayIntent
                    )

                val playIntent1 = Intent(this, ForegroundService::class.java)
                playIntent1.action = "ACTION_STOP"
                val pendingPlayIntent1 = PendingIntent.getService(this, 11122, playIntent1, 0)
                val playAction1 =
                    NotificationCompat.Action(
                        android.R.drawable.ic_media_pause,
                        "Stop",
                        pendingPlayIntent1
                    )


                val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(getString(R.string.title_notif))
                    .setContentText(input)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(pendingIntent)
                    .addAction(playAction)
                    .addAction(playAction1)
                    .build()
                startForeground(1, notification)
                startTraining()
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


            wordsToAlarm?.let { addAlarms(it) }

        }
    }

      private suspend fun getListFromBase(): List<Word> {
         val db = Room.databaseBuilder(
             applicationContext,
             WordRoomDatabase::class.java, "word_database"
         ).build()

         var words: List<Word> = db.wordDao().getRandWords()
         return words
     }
      fun addAlarms(list_data: List<Word>) {

        val am: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    //    val time2 = 1L//число минут между повторами
          val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)
          val timeString = sharedPreferences.getString("time_repeat", "15")

          val countString = sharedPreferences.getString("countLoop", "55")
          val oldDevBoolean = sharedPreferences.getBoolean("oldBandMaintain" ,false)


        val time2 = timeString?.toLong()//число минут между повторами
        var count = countString?.toInt()//число повторов
        val time1: Long = 1000 * 60 * time2!!
        var time = 1000 * 60L

          if (count != null) {
              if (count > list_data.size) count = list_data.size
          }

        for (x in 0 until count!!) {
            val phrase: String = list_data[x].word + "     =      " + list_data[x].translate

            var phraseIntent = Intent("com.spotolcom.easyrepeater.MyReceiver")
            phraseIntent.putExtra("prase", phrase)


            var pendingPhraseIntent = PendingIntent.getBroadcast(this, x, phraseIntent, 0)
            am[AlarmManager.RTC, System.currentTimeMillis() + time] = pendingPhraseIntent

                //здесь сделать проверку в настройках поддерживать ли старый банд
            if(oldDevBoolean){
                var intent_prase_band: Intent = Intent("com.uthink.ring.ACTION_INCOMING_RINGING")
                intent_prase_band.putExtra("incoming_number",phrase)
                val pendingPhraseIntentOld = PendingIntent.getBroadcast(this, x+700, intent_prase_band, 0)
                am[AlarmManager.RTC, System.currentTimeMillis() + time+7000] = pendingPhraseIntentOld
            }
            time += time1
        }
          Toast.makeText(this, getString(R.string.startsAlarms), Toast.LENGTH_LONG).show()
    }

    private fun stopTraining() {

        val am1 = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)

        val countString = sharedPreferences.getString("countLoop", "35")

     //здесь надо ввести колличество установленных алармов
        if (countString != null) {
            for (i in 0 until countString.toInt()) {

                var intent_prase_band1 = Intent("com.spotolcom.easyrepeater.MyReceiver")

                val pendingPhraseIntent = PendingIntent.getBroadcast(
                    this,
                    i,
                    intent_prase_band1,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                if (pendingPhraseIntent != null) {
                    am1.cancel(pendingPhraseIntent)
                }
            }
        }
        stopSelf();
        Toast.makeText(this, getString(R.string.stopAlarms), Toast.LENGTH_LONG).show()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, getString(R.string.ForegroundServiceChannel),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }
}

package com.spotolcom.easyrepeater

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
       Log.d("mytag", "16;onReceive: Onreciev "+intent.getStringExtra("prase"))
        
//провперить экстра на пустую строку и если пустая взять рандомную фразу из базы

        creatNotif(context,intent.getStringExtra("prase"))
        //startActivity(Intent(context, MyAppMainActivity::class.java))
    }

    private fun creatNotif(context: Context, phrase: String?){
        var phraseString = phrase
        var phrase1: String =""
        if(phrase == ""){
            runBlocking {
                var wordsToAlarm: List<Word>? =null
                val job = launch(this.coroutineContext) { //(2)
                    wordsToAlarm  = getListFromBase(context) //(3)
                }
                job.join() //(4)
                Log.d("mytag", "104;startTraining: $wordsToAlarm")
               // wordsToAlarm?.let { addAlarms(it) }

                 phrase1 = wordsToAlarm!![0].word + "     =      " + wordsToAlarm!![0].translate
            }

            phraseString = phrase1
        }

        var builder = NotificationCompat.Builder(context, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
             // .setContentTitle(".")
            .setContentText(phraseString)
            .setPriority(NotificationCompat.PRIORITY_MAX)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(111, builder.build())
        }}

    private suspend fun getListFromBase(context: Context): List<Word> {
        val db = Room.databaseBuilder(
            context,
            WordRoomDatabase::class.java, "word_database"
        ).build()

        var words: List<Word> = db.wordDao().getRandWords()
//         Log.d("mytag", "94;startTraining: $words")
        return words
    }
}
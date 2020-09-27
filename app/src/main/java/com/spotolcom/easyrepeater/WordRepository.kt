package com.spotolcom.easyrepeater

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spotolcom.easyrepeater.data.Phrase

class WordRepository(private val wordDao: WordDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    //val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords()
    val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords()

    private val _allPhrases = MutableLiveData<List<Phrase>>().apply {
      value =   listOf(Phrase("tkdfkj","transl"),
            Phrase("tkdfkw2j","tranrr4sl"),Phrase("tkd3fkj","tran22sl"))
    }
    val allPhrases: LiveData<List<Phrase>> = _allPhrases


//    val allPhrases: LiveData<List<Phrase>> = LiveData(listOf(Phrase("tkdfkj","transl"),
//        Phrase("tkdfkw2j","tranrr4sl"),Phrase("tkd3fkj","tran22sl")))

    // You must call this on a non-UI thread or your app will crash. So we're making this a
    // suspend function so the caller methods know this.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getRandWords() {
        wordDao.getRandWords()
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun upDate(id:String,word:String,traslate:String) {
        wordDao.update(id,word,traslate)
    }
}
package com.spotolcom.easyrepeater.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.spotolcom.easyrepeater.data.Word
import com.spotolcom.easyrepeater.data.WordDao

class WordRepository(private val wordDao: WordDao) {

    val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords()

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


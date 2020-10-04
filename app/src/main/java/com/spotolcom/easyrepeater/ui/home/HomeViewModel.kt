package com.spotolcom.easyrepeater.ui.home

import android.app.Application
import androidx.lifecycle.*
import com.spotolcom.easyrepeater.data.Word
import com.spotolcom.easyrepeater.data.WordRepository
import com.spotolcom.easyrepeater.data.WordRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WordRepository
    val allWords: LiveData<List<Word>>

    init {
        val wordsDao = WordRoomDatabase.getDatabase(application, viewModelScope).wordDao()
        repository = WordRepository(wordsDao)
        allWords = repository.allWords
    }

    fun insert(word: Word) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(word)
    }
    fun upDate(id:String,word:String,traslate:String) = viewModelScope.launch(Dispatchers.IO) {
        repository.upDate(id,word,traslate)
    }
}
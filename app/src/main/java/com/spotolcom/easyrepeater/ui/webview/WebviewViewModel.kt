package com.spotolcom.easyrepeater.ui.webview

import android.app.Application
import androidx.lifecycle.*
import com.spotolcom.easyrepeater.Word
import com.spotolcom.easyrepeater.WordRepository
import com.spotolcom.easyrepeater.WordRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//class HomeViewModel(application: Application) : AndroidViewModel(application) {
class WebviewViewModel(application: Application) : AndroidViewModel(application) {
//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text

    private val repository: WordRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allWords: LiveData<List<Word>>

    init {
        val wordsDao = WordRoomDatabase.getDatabase(application, viewModelScope).wordDao()
        repository = WordRepository(wordsDao)
        allWords = repository.allWords
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(word: Word) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(word)
    }
    fun getRandWords() = viewModelScope.launch(Dispatchers.IO) {
        repository.getRandWords()
    }
    fun upDate(id:String,word:String,traslate:String) = viewModelScope.launch(Dispatchers.IO) {
        repository.upDate(id,word,traslate)
    }
}
package com.spotolcom.easyrepeater.ui.webview

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.spotolcom.easyrepeater.Word
import com.spotolcom.easyrepeater.WordRepository
import com.spotolcom.easyrepeater.WordRoomDatabase
import com.spotolcom.easyrepeater.data.Phrase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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
     var allWords: LiveData<List<PhraseFromServer>>? = null

    init {
        val wordsDao = WordRoomDatabase.getDatabase(application, viewModelScope).wordDao()
        repository = WordRepository(wordsDao)


        val service = ApiFactory.placeholderApi

        //Getting Posts from Jsonplaceholder API
        GlobalScope.launch(Dispatchers.Main) {
            val postRequest = service.getPhotos()
            try {
                Log.d("mytag", "84;onActivityCreated: try")
                val response = postRequest.await()
                if(response.isSuccessful){

                    val posts = response.body()
                    if (posts != null) {
                        Log.d("mytag", "101;onActivityCreated: ${posts.size} ${posts.get(1)} ")

//                        posts.forEach {
//                            println("The element is ${it.phrase} ${it.translate}")
//                        }
                        val _allPhrasesServer = MutableLiveData<List<PhraseFromServer>>().apply {
                            value =   posts }
                        allWords =_allPhrasesServer


                    }
                    Log.d("mytag", "92;onActivityCreated: $posts")
                }else{
                    allWords = null
                    Log.d("mytag ","96"+response.errorBody().toString())
                }

            }catch (e: Exception){

            }
        }
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
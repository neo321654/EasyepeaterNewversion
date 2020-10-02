package com.spotolcom.easyrepeater.ui.webview

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.spotolcom.easyrepeater.Word
import com.spotolcom.easyrepeater.WordRepository
import com.spotolcom.easyrepeater.WordRoomDatabase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

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
     var allWords: LiveData<List<PhraseFromServer>>

    init {
        val wordsDao = WordRoomDatabase.getDatabase(application, viewModelScope).wordDao()
        repository = WordRepository(wordsDao)


         val _allPhrases = MutableLiveData<List<PhraseFromServer>>().apply {
            value =   listOf(PhraseFromServer("tkdfkj","transl"),
                PhraseFromServer("tkdfkw2j","tranrr4sl"),PhraseFromServer("tkd3fkj","tran22sl"))
        }
        allWords = _allPhrases
        Log.d("mytag", "46;: $allWords")

     //  allWords.postValue(go_to_server())
    }

     fun go_to_server(): MutableLiveData<List<PhraseFromServer>>? {
        val service = ApiFactory.placeholderApi
        //Getting Posts from Jsonplaceholder API
        var _allPhrasesServer : MutableLiveData<List<PhraseFromServer>>? = null

        GlobalScope.launch(Dispatchers.Main) {
            val postRequest = service.getPhotos()
            try {
                val response = postRequest.await()
                if (response.isSuccessful) {
                    val posts = response.body()
                    if (posts != null) {
                        Log.d("mytag", "101;onActivityCreated: ${posts.size} ${posts.get(1)} ")
    //                        posts.forEach {
    //                            println("The element is ${it.phrase} ${it.translate}")
    //                        }
                         _allPhrasesServer = MutableLiveData<List<PhraseFromServer>>().apply {
                            value = posts
                        }
                       // allWords.setvalue(_allPhrasesServer)
                    }
                    Log.d("mytag", "92;onActivityCreated: $allWords")
                } else {
                    Log.d("mytag ", "96" + response.errorBody().toString())
                }
            } catch (e: Exception) {
            }
        }

         return _allPhrasesServer
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


    object ApiFactory {
        val placeholderApi : PlaceholderApi = RetrofitFactory.retrofit(AppConstants.JSON_PLACEHOLDER_BASE_URL)
            .create(PlaceholderApi::class.java)
    }
    interface PlaceholderApi{
        @GET("/add_word.php")
        fun getPhotos() : Deferred<Response<List<PhraseFromServer>>>
        // fun getPhotos() : String
    }
    data class PhraseFromServer(
        val phrase: String,
        val translate: String
    )
    object RetrofitFactory{
        private val authInterceptor = Interceptor {chain->
            val newUrl = chain.request().url()
                .newBuilder()
                //  .addQueryParameter("api_key", AppConstants.tmdbApiKey)
                .build()

            val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }

        private val loggingInterceptor =  HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        //Not logging the authkey if not debug
        private val client =
            // if(BuildConfig.DEBUG){
            if(true){
                OkHttpClient().newBuilder()
                    .addInterceptor(authInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .build()
            }else{
                OkHttpClient().newBuilder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(authInterceptor)
                    .build()
            }

        fun retrofit(baseUrl : String) : Retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    }
    object AppConstants{
        //  const val JSON_PLACEHOLDER_BASE_URL = "https://jsonplaceholder.typicode.com"
        const val JSON_PLACEHOLDER_BASE_URL = "http://srv34889.ht-test.ru"
    }

}
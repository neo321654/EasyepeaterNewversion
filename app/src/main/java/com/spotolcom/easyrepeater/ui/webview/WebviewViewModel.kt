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

class WebviewViewModel(application: Application) : AndroidViewModel(application) {

     var allWords: LiveData<List<PhraseFromServer>>

    init {

         val _allPhrases = MutableLiveData<List<PhraseFromServer>>().apply {
            value =   listOf(PhraseFromServer("tkdfkj","transl"),
                PhraseFromServer("tkdfkw2j","tranrr4sl"),PhraseFromServer("tkd3fkj","tran22sl"))
        }
        allWords = _allPhrases

    }

     fun go_to_server(): MutableLiveData<List<PhraseFromServer>>? {
        val service = ApiFactory.placeholderApi

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

                      //  allWords = _allPhrasesServer!!
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
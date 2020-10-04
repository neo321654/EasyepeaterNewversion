package com.spotolcom.easyrepeater.ui.webview

import android.app.Application
import androidx.lifecycle.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

class WebviewViewModel(application: Application) : AndroidViewModel(application) {

     val allWords: MutableLiveData<List<PhraseFromServer>> by lazy {
         MutableLiveData<List<PhraseFromServer>> ()
     }
    val currentName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    init {

         val _allPhrases =  listOf(PhraseFromServer("tkdfkj","transl"),
                PhraseFromServer("tkdfkw2j","tranrr4sl"),PhraseFromServer("tkd3fkj","tran22sl"))

        allWords.value = _allPhrases
        currentName.value = "000"

    }

     fun go_to_server(){
        val service = ApiFactory.placeholderApi

         viewModelScope.launch {
             val postRequest = service.getPhrases()
             try {
                 val response = postRequest.await()
                 if (response.isSuccessful) {
                     val posts = response.body()
                     if (posts != null) {
                         allWords.value =posts
                     }
                 }
             } catch (e: Exception) {
             }
         }
     }

    object ApiFactory {
        val placeholderApi : PlaceholderApi = RetrofitFactory.retrofit("http://srv34889.ht-test.ru")
            .create(PlaceholderApi::class.java)
    }
    interface PlaceholderApi{
        @GET("/add_word.php")
        fun getPhrases() : Deferred<Response<List<PhraseFromServer>>>
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

}
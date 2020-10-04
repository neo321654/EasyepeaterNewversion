package com.spotolcom.easyrepeater.ui.serverFragment

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Room
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.spotolcom.easyrepeater.data.Word
import com.spotolcom.easyrepeater.data.WordRoomDatabase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

class ServerViewModel(application: Application) : AndroidViewModel(application) {

     val allWords: MutableLiveData<List<PhraseFromServer>> by lazy {
         MutableLiveData<List<PhraseFromServer>> ()
     }

    init {
         val _allPhrases =  listOf(PhraseFromServer("tkdfkj","transl"),
                PhraseFromServer("tkdfkw2j","tranrr4sl"),PhraseFromServer("tkd3fkj","tran22sl"))
        allWords.value = _allPhrases
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

    fun insertIfNotPhrase(Ctx: Context) {
        Log.d("mytag", "107;insertIfNotPhrase: click insert")
        viewModelScope.launch (Dispatchers.IO) {
            val db = Room.databaseBuilder(
                Ctx,
                WordRoomDatabase::class.java, "word_database"
            ).build()

            var words: List<Word> = db.wordDao().getRandWords()
            var listWords : List<PhraseFromServer>? = allWords.value


            var wordsServer = listWords?.map {    Word(word = it.phrase,translate = it.translate)}
            var wordsBase = words.map {  Word(word = it.word,translate = it.translate)}
         //   var minusWords =wordsServer.minusAssign(wordsBase)
            Log.d("mytag", "120;wordsServer: $wordsServer")
            Log.d("mytag", "120;wordsBase: $wordsBase")

//            for (word in words) {
//                //Log.d("mytag", "115;insertIfNotPhrase: ${word.word} ${word.translate}")
//
//                listWords?.let{
//                    val size = it.size
//
//                    for (i in 0 until size) {
//
//                        if(listWords[i].phrase != word.word){
//
//                                db.wordDao().insert(
//                                    Word(
//                                        word = listWords[i].phrase,
//                                        translate = listWords[i].translate
//                                    )
//                                )
//            Log.d("mytag", "123;insertIfNotPhrase: ${listWords[i].phrase} ==${word.word} ")
//                             }
//
//                        }
//                    }
//                }






                }
            }
        }





package com.spotolcom.easyrepeater.ui.webview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.spotolcom.easyrepeater.R
import com.spotolcom.easyrepeater.data.WordListAdapterPhrase
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


class webview : Fragment() {
    private lateinit var webViewModel: WebviewViewModel

    private lateinit var viewModel: WebviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        webViewModel =ViewModelProvider(this).get(WebviewViewModel::class.java)

        val root :View = inflater.inflate(R.layout.webview_fragment, container, false)

        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = WordListAdapterPhrase(root.context)

        recyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(root.context)
        //layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        recyclerView.layoutManager = layoutManager

        webViewModel.allWords?.observe(viewLifecycleOwner, Observer { words ->
            // Update the cached copy of the words in the adapter.

            words?.let { adapter.setWords(it) }
        })

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WebviewViewModel::class.java)
        // TODO: Use the ViewModel

    //    CookieSyncManager.createInstance(this)
//        val cookieManager: CookieManager = CookieManager.getInstance()
//        cookieManager.setAcceptCookie(true)
//
//        val webview = WebView(context)
//        val ws = webview.settings
//        ws.saveFormData = true
//        ws.savePassword = true // Not needed for API level 18 or greater (deprecated)
//
//
    //    WebAction(webview)

//        webView.webViewClient = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                if (url != null) {
//                    view?.loadUrl(url)
//                }
//                return true
//            }
//        }

      //  webView.loadUrl("https://www.avito.ru/")

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

                    }
                    Log.d("mytag", "92;onActivityCreated: $posts")
                }else{
                    Log.d("mytag ","96"+response.errorBody().toString())
                }

            }catch (e: Exception){

            }
        }
    }
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

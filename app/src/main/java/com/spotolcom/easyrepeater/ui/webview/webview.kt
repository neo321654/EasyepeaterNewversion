package com.spotolcom.easyrepeater.ui.webview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.spotolcom.easyrepeater.ForegroundService
import com.spotolcom.easyrepeater.R
import com.spotolcom.easyrepeater.data.WordListAdapterPhrase
import kotlinx.android.synthetic.main.webview_fragment.*
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

    private lateinit var adapter: WordListAdapterPhrase
    private lateinit var viewModel: WebviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(WebviewViewModel::class.java)
        val root :View = inflater.inflate(R.layout.webview_fragment, container, false)


       adapter = WordListAdapterPhrase(root.context)
        recyclerview.adapter = adapter
        val layoutManager = LinearLayoutManager(root.context)
        //layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        recyclerview.layoutManager = layoutManager
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.allWords.observe(viewLifecycleOwner, Observer { words ->
            words?.let { adapter.setWords(it) }
        })

        sync.setOnClickListener{
            // viewModel.allWords = viewModel.go_to_server()!!
            Log.d("mytag", "67;onActivityCreated: click")
            val _allPhrases = MutableLiveData<List<WebviewViewModel.PhraseFromServer>>().apply {
                value =   listOf(
                    WebviewViewModel.PhraseFromServer("11", "22"),
                    WebviewViewModel.PhraseFromServer("22", "22"),
                    WebviewViewModel.PhraseFromServer("222", "333")
                )
            }
            viewModel.allWords =_allPhrases

        }


    }
}

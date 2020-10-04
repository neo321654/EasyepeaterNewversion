package com.spotolcom.easyrepeater.ui.serverFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.spotolcom.easyrepeater.R
import com.spotolcom.easyrepeater.data.WordListAdapterPhrase
import kotlinx.android.synthetic.main.webview_fragment.*


class ServerFragment : Fragment() {

    private lateinit var adapter: WordListAdapterPhrase
    private lateinit var viewModel: ServerViewModel
    lateinit var cont : Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container != null) {
            cont = requireContext()
        }
        return inflater.inflate(R.layout.webview_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ServerViewModel::class.java)

        adapter = WordListAdapterPhrase(cont)
        recyclerview.adapter = adapter
        val layoutManager = LinearLayoutManager(cont)
        //layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        recyclerview.layoutManager = layoutManager

        viewModel.allWords.observe(viewLifecycleOwner, Observer { words ->
            words?.let { adapter.setWords(it) }
        })
        viewModel.currentName.observe(viewLifecycleOwner, Observer { wordss ->
            text_home.text=wordss
        })
        download_to_base.setOnClickListener{

        }
        sync.setOnClickListener{
            viewModel.go_to_server()
        }
    }
}

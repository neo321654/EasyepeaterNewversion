package com.spotolcom.easyrepeater.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.spotolcom.easyrepeater.ForegroundService
import com.spotolcom.easyrepeater.R
import com.spotolcom.easyrepeater.WordListAdapter

class HomeFragment() : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//
//        wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)

        homeViewModel =ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = WordListAdapter(root.context)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(root.context)

        homeViewModel.allWords.observe(viewLifecycleOwner, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.setWords(it) }
        })
        val fab = root.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_gallery, null))
        val button_start = root.findViewById<Button>(R.id.start_btn)
        val button_stop = root.findViewById<Button>(R.id.stop_btn)
        button_start.setOnClickListener{
//            creatNotif(it.context)
            ForegroundService.startService(root.context, getString(R.string.text_notif))

        }
        button_stop.setOnClickListener{
            ForegroundService.stopService(root.context)

        }
        return root
    }


}
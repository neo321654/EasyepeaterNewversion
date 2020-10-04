package com.spotolcom.easyrepeater.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.spotolcom.easyrepeater.ForegroundService
import com.spotolcom.easyrepeater.R
import com.spotolcom.easyrepeater.data.WordListAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var cont : Context

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        if (container != null) {
            cont = requireContext()
        }
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        homeViewModel =ViewModelProvider(this).get(HomeViewModel::class.java)
        val adapter = WordListAdapter(cont)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(cont)
        homeViewModel.allWords.observe(viewLifecycleOwner, Observer { words ->
            words?.let { adapter.setWords(it) }
        })

        fab.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_gallery, null))

        start_btn.setOnClickListener{
            ForegroundService.startService(cont, getString(R.string.text_notif))
        }
        stop_btn.setOnClickListener{
            ForegroundService.stopService(cont)
        }
    }

}
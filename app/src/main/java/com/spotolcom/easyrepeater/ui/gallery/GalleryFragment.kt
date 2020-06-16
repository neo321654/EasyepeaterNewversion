package com.spotolcom.easyrepeater.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.spotolcom.easyrepeater.R
import com.spotolcom.easyrepeater.Word
import com.spotolcom.easyrepeater.ui.home.HomeViewModel

class GalleryFragment : Fragment() {

//    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var editWordView: EditText
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
//        val textView: TextView = root.findViewById(R.id.text_gallery)
//        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        editWordView = root.findViewById(R.id.edit_word)
        homeViewModel =ViewModelProvider(this).get(HomeViewModel::class.java)
        val button = root.findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val word = Word(word = editWordView.text.toString())
            homeViewModel.insert(word)
            it.findNavController().navigate(R.id.nav_home, null)
        }



        return root
    }
}
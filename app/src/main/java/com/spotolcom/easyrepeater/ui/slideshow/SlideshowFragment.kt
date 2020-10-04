package com.spotolcom.easyrepeater.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.spotolcom.easyrepeater.R
import com.spotolcom.easyrepeater.ui.home.HomeViewModel

class SlideshowFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
        val id: TextView = root.findViewById(R.id.id_text)
        val edit_word: EditText = root.findViewById(R.id.edit_word)
        val edit_translate: EditText = root.findViewById(R.id.edit_translate)
        val save_btn: Button = root.findViewById(R.id.button_save)
        homeViewModel =ViewModelProvider(this).get(HomeViewModel::class.java)

        id.text = arguments?.getString("id")
        edit_word.setText( arguments?.getString("word"))
        edit_translate.setText(arguments?.getString("translate"))

        save_btn.setOnClickListener{
            homeViewModel.upDate(id.text.toString(),edit_word.text.toString(),edit_translate.text.toString())
            it.findNavController().navigate(R.id.nav_home, null)
        }

        return root
    }
}
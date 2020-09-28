package com.spotolcom.easyrepeater.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.spotolcom.easyrepeater.R
import com.spotolcom.easyrepeater.ui.webview.PhraseFromServer


class WordListAdapterPhrase internal constructor(
    context: Context
) : RecyclerView.Adapter<WordListAdapterPhrase.WordViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var words = emptyList<PhraseFromServer>() // Cached copy of words

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val word: TextView = itemView.findViewById(R.id.word)
        val id: TextView = itemView.findViewById(R.id.id)
        val translate: TextView = itemView.findViewById(R.id.translate)
        val edit_phrase: ImageButton = itemView.findViewById(R.id.edit_phrase)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = words[position]
        holder.word.text = current.phrase

        holder.id.text =(position+1).toString()
        holder.translate.text = current.translate
        holder.edit_phrase.setOnClickListener{

            val bundle = bundleOf(
                "word" to current.phrase,
//                "id" to current.id.toString(),
                "translate" to current.translate)
//            view.findNavController().navigate(R.id.confirmationAction, bundle)
            it.findNavController().navigate(R.id.nav_slideshow, bundle)
        }


    }

    internal fun setWords(words: List<PhraseFromServer>) {
        this.words = words
//        Log.d("mytag", "35;setWords: $words")
        notifyDataSetChanged()
    }

    override fun getItemCount() = words.size
}

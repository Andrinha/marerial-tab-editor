package com.example.marerialtabeditor.adapters

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marerialtabeditor.R
import com.example.marerialtabeditor.databinding.ItemSongBinding
import com.example.marerialtabeditor.repository.data.Song
import com.example.marerialtabeditor.utils.themeColor
import java.util.*
import kotlin.properties.Delegates

class SongAdapter : RecyclerView.Adapter<SongAdapter.ViewHolder>() {
    private var list = emptyList<Song>()
    private var id by Delegates.notNull<Int>()
    private var query = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], query)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setQuery(query: String) {
        this.query = query
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Song>) {
        this.list = data
        notifyDataSetChanged()
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ItemSongBinding.bind(item)
        fun bind(song: Song, query: String) = with(binding) {
            textName.text = song.name
            textBand.text = song.band
//            cardMain.setOnClickListener {
//                itemView.findNavController().navigate(
//                    ArchiveFragmentDirections.actionArchiveFragmentToEditFragment(ActionEnum.EDIT, Tab(tabId = id, song = song))
//                )
//            }
//            cardMain.setOnLongClickListener {
//                cardMain.isChecked = !cardMain.isChecked
//                true
//            }

            if (query.isNotEmpty()) {
                val indexName = song.name.indexOf(query, ignoreCase = true)
                if (indexName >= 0) {
                    val content = SpannableString(textName.text)
                    content.setSpan(
                        ForegroundColorSpan(itemView.context.themeColor(com.google.android.material.R.attr.colorTertiary)),
                        indexName,
                        indexName + query.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    textName.text = content
                }

                val indexBand = song.band.indexOf(query, ignoreCase = true)
                if (indexBand >= 0) {
                    val content = SpannableString(textBand.text)
                    content.setSpan(
                        ForegroundColorSpan(itemView.context.themeColor(com.google.android.material.R.attr.colorTertiary)),
                        indexBand,
                        indexBand + query.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    textBand.text = content
                }
            }
        }
    }
}
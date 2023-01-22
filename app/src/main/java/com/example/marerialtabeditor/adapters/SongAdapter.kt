package com.example.marerialtabeditor.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marerialtabeditor.R
import com.example.marerialtabeditor.databinding.ItemSongBinding
import com.example.marerialtabeditor.repository.data.Song
import java.util.*

class SongAdapter : RecyclerView.Adapter<SongAdapter.ViewHolder>() {
    private var data = emptyList<Song>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Song>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ItemSongBinding.bind(item)
        fun bind(song: Song) = with(binding) {
            textName.text = song.name
            textBand.text = song.band
            cardMain.setOnLongClickListener {
                cardMain.isChecked = !cardMain.isChecked
                true
            }
        }
    }
}
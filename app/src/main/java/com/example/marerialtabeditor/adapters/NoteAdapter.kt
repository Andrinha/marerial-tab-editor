package com.example.marerialtabeditor.adapters

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marerialtabeditor.R
import com.example.marerialtabeditor.databinding.ItemNoteBinding
import com.example.marerialtabeditor.repository.data.Note
import com.example.marerialtabeditor.utils.themeColor
import java.util.*

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private var data = mutableListOf<Note>()
    private var focus: Int = -1
    private var column: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], focus, position, column)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Note>) {
        this.data = data.toMutableList()
        notifyDataSetChanged()
    }

    fun setNote(position: Int, note: Note) {
        this.data[position] = note
        notifyItemChanged(position)
    }

    fun getNote(position: Int): Note {
        return data[position]
    }

    fun setFocus(focus: Int) {
        val temp = this.focus
        this.focus = focus
        notifyItemChanged(temp)
        notifyItemChanged(focus)
    }

    fun setColumn(column: Int) {
        this.column = column
        if (column == -1)
            notifyItemRangeChanged(data.size - 6, 6)
        else
            notifyItemRangeChanged(column * 6 - 6, 12)
    }


    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ItemNoteBinding.bind(item)

        fun bind(note: Note, focus: Int, position: Int, column: Int) = with(binding) {
            if (position.mod(48) in 0..5)
                viewDivider.visibility = View.VISIBLE else
                viewDivider.visibility = View.GONE
            textFret.apply {
                text = if (position == focus) note.toString()
                else SpannableString(note.toString()).apply {
                    setSpan(
                        BackgroundColorSpan(itemView.context.themeColor(com.google.android.material.R.attr.colorPrimaryContainer)),
                        0,
                        note.toString().length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                background = if (position == focus && position !in column * 6 until column * 6 + 6) {
                    itemView.context.theme.getDrawable(R.drawable.background_focus)
                } else {
                    null
                }
            }
            layoutFret.background = if (position in column * 6 until column * 6 + 6) {
                itemView.context.theme.getDrawable(R.drawable.background_playing)
            } else null
        }
    }
}
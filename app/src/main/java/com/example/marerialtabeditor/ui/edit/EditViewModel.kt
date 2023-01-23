package com.example.marerialtabeditor.ui.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marerialtabeditor.repository.data.Note
import com.example.marerialtabeditor.repository.data.Song

class EditViewModel : ViewModel() {
    val song = MutableLiveData(Song(mutableListOf()).apply {
        addEmptyChunk()
        addEmptyChunk()
        addEmptyChunk()
        addEmptyChunk()
    })
    val focus = MutableLiveData<Int>()

    fun setNote(position: Int, note: Note) {
        this.song.value!!.notes[position] = note
        focus.value = focus.value!!
    }
}
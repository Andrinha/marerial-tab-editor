package com.example.marerialtabeditor.ui.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marerialtabeditor.repository.data.Song

class EditViewModel : ViewModel() {
    val song = MutableLiveData(Song(mutableListOf(), "name", "band").apply {
        addEmptyChunk()
        addEmptyChunk()
        addEmptyChunk()
        addEmptyChunk()

        repeat(10000) {
            addEmptyChunk()
        }
    })
    val focus = MutableLiveData<Int>()
}
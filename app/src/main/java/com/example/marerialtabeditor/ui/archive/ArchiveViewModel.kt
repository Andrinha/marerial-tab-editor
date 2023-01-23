package com.example.marerialtabeditor.ui.archive

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marerialtabeditor.repository.data.Song
import com.example.marerialtabeditor.utils.Hardcoded

class ArchiveViewModel : ViewModel() {
    val searchQuery: MutableLiveData<String> = MutableLiveData("")
    val songs: MutableLiveData<List<Song>> = MutableLiveData(Hardcoded().songs)
}

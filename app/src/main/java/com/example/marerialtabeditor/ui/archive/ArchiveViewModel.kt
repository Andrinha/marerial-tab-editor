package com.example.marerialtabeditor.ui.archive

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.marerialtabeditor.repository.data.Song
import com.example.marerialtabeditor.repository.data.tab.AppDatabase

class ArchiveViewModel(application: Application) : AndroidViewModel(application) {
    val searchQuery: MutableLiveData<String> = MutableLiveData("")
    val songs: MutableLiveData<List<Song>> = MutableLiveData()

    private val dao = AppDatabase.getDatabase(application).tabDao()
    val tabs = dao.readAllData()

}

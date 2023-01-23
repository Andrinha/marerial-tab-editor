package com.example.marerialtabeditor.ui.archive

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.marerialtabeditor.repository.data.Song
import com.example.marerialtabeditor.repository.data.tab.AppDatabase
import com.example.marerialtabeditor.utils.Hardcoded

class ArchiveViewModel(application: Application) : AndroidViewModel(application) {
    val searchQuery: MutableLiveData<String> = MutableLiveData("")
    val songs: MutableLiveData<List<Song>> = MutableLiveData(Hardcoded().songs)

    private val dao = AppDatabase.getDatabase(application).tabDao()
    val tabs = dao.readAllData()

}

package com.example.marerialtabeditor.ui.edit

import android.app.Application
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.marerialtabeditor.repository.data.Note
import com.example.marerialtabeditor.repository.data.Song
import com.example.marerialtabeditor.repository.data.tab.Tab
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

class EditViewModel(application: Application) : AndroidViewModel(application) {
    val tab = MutableLiveData(Tab(0, Song(mutableListOf(), "", "").apply {
        addEmptyChunk()
        addEmptyChunk()
        addEmptyChunk()
        addEmptyChunk()
    }))
    val focus = MutableLiveData<Int>()
    val loaded = MutableLiveData(mutableListOf<Int>())
    private var streamID = 0
    private val assetManager: AssetManager by lazy {
        application.assets
    }
    private val soundPool: SoundPool by lazy {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        SoundPool.Builder()
            .setAudioAttributes(attributes)
            .setMaxStreams(10)
            .build()
    }

    init {
        loadSounds()
    }

    private fun loadSounds() {
//        loaded.value!!.add(loadSound("2e.mp3"))
//        loaded.value!!.add(loadSound("2f.mp3"))
//        loaded.value!!.add(loadSound("2fd.mp3"))
//        loaded.value!!.add(loadSound("2g.mp3"))
//        loaded.value!!.add(loadSound("2gd.mp3"))
//        loaded.value!!.add(loadSound("2a.mp3"))
//        loaded.value!!.add(loadSound("2ad.mp3"))
//        loaded.value!!.add(loadSound("2b.mp3"))
//
//        loaded.value!!.add(loadSound("3c.mp3"))
//        loaded.value!!.add(loadSound("3cd.mp3"))
//        loaded.value!!.add(loadSound("3d.mp3"))
//        loaded.value!!.add(loadSound("3dd.mp3"))
        loaded.value!!.add(loadSound("3e.mp3"))
        loaded.value!!.add(loadSound("3f.mp3"))
        loaded.value!!.add(loadSound("3fd.mp3"))
        loaded.value!!.add(loadSound("3g.mp3"))
        loaded.value!!.add(loadSound("3gd.mp3"))
        loaded.value!!.add(loadSound("3a.mp3"))
        loaded.value!!.add(loadSound("3ad.mp3"))
        loaded.value!!.add(loadSound("3b.mp3"))

        loaded.value!!.add(loadSound("4c.mp3"))
        loaded.value!!.add(loadSound("4cd.mp3"))
        loaded.value!!.add(loadSound("4d.mp3"))
        loaded.value!!.add(loadSound("4dd.mp3"))
        loaded.value!!.add(loadSound("4e.mp3"))
        loaded.value!!.add(loadSound("4f.mp3"))
        loaded.value!!.add(loadSound("4fd.mp3"))
        loaded.value!!.add(loadSound("4g.mp3"))
        loaded.value!!.add(loadSound("4gd.mp3"))
        loaded.value!!.add(loadSound("4a.mp3"))
        loaded.value!!.add(loadSound("4ad.mp3"))
        loaded.value!!.add(loadSound("4b.mp3"))

        loaded.value!!.add(loadSound("5c.mp3"))
        loaded.value!!.add(loadSound("5cd.mp3"))
        loaded.value!!.add(loadSound("5d.mp3"))
        loaded.value!!.add(loadSound("5dd.mp3"))
        loaded.value!!.add(loadSound("5e.mp3"))
        loaded.value!!.add(loadSound("5f.mp3"))
        loaded.value!!.add(loadSound("5fd.mp3"))
        loaded.value!!.add(loadSound("5g.mp3"))
        loaded.value!!.add(loadSound("5gd.mp3"))
        loaded.value!!.add(loadSound("5a.mp3"))
        loaded.value!!.add(loadSound("5ad.mp3"))
        loaded.value!!.add(loadSound("5b.mp3"))

        loaded.value!!.add(loadSound("6c.mp3"))
        loaded.value!!.add(loadSound("6cd.mp3"))
        loaded.value!!.add(loadSound("6d.mp3"))
        loaded.value!!.add(loadSound("6dd.mp3"))
        loaded.value!!.add(loadSound("6e.mp3"))
        loaded.value!!.add(loadSound("6f.mp3"))
        loaded.value!!.add(loadSound("6fd.mp3"))
        loaded.value!!.add(loadSound("6g.mp3"))
        loaded.value!!.add(loadSound("6gd.mp3"))
        loaded.value!!.add(loadSound("6a.mp3"))
        loaded.value!!.add(loadSound("6ad.mp3"))
        loaded.value!!.add(loadSound("6b.mp3"))

        loaded.value!!.add(loadSound("7c.mp3"))
        loaded.value!!.add(loadSound("7cd.mp3"))
        loaded.value!!.add(loadSound("7d.mp3"))
        loaded.value!!.add(loadSound("7dd.mp3"))
        loaded.value!!.add(loadSound("7e.mp3"))    }

    fun setNote(position: Int, note: Note) {
        this.tab.value!!.song.notes[position] = note
        focus.value = focus.value!!
    }

    private fun loadSound(fileName: String): Int {
        val afd: AssetFileDescriptor = try {
            assetManager.openFd(fileName)
        } catch (e: IOException) {
            return -1
        }
        return soundPool.load(afd, 1)
    }

    fun playSound(sound: Int): Int {
        if (sound > 0) {
            streamID = soundPool.play(sound, 1F, 1F, 1, 0, 1F)
        }
        return streamID
    }

    fun playSong() {
        viewModelScope.launch {
            val size = tab.value!!.song.notes.size
            repeat(size / 6) { y ->
                repeat(6) { x ->
                    val position = x + 6 * y
                    val note = tab.value!!.song.notes[position]
                    if (note.fret != -1)
                        playSound(loaded.value?.getOrNull(note.fret + getOffset(position)) ?: -1)
                }
                delay(100)
            }
        }
    }

    fun getOffset(focus: Int): Int =
        when (focus.mod(6)) {
            0 -> 24
            1 -> 19
            2 -> 15
            3 -> 10
            4 -> 5
            5 -> 0
            else -> 0
        }
}
package com.example.marerialtabeditor.ui.edit

import android.app.Application
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.marerialtabeditor.repository.data.Note
import com.example.marerialtabeditor.repository.data.Song
import com.example.marerialtabeditor.repository.data.tab.Tab
import java.io.IOException

class EditViewModel(application: Application) : AndroidViewModel(application) {
    val tab = MutableLiveData(Tab(0, Song(mutableListOf(), "", "", 120).apply {
        addEmptyChunk()
        addEmptyChunk()
        addEmptyChunk()
        addEmptyChunk()
    }))
    val focus = MutableLiveData<Int>()
    val playingColumn = MutableLiveData(-1)
    val selectedColumn = MutableLiveData(-1)
    val loaded = MutableLiveData(mutableListOf<Int>())
    val loadedHarmonics = MutableLiveData(mutableListOf<Int>())
    val copiedColumn = MutableLiveData(Array(6) { Note(0) })
    val muteSound = MutableLiveData(-1)
    val isPlaying = MutableLiveData(false)

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
        muteSound.value = loadSound("muted/muted.mp3")

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
        loaded.value!!.add(loadSound("normal/3e.mp3"))
        loaded.value!!.add(loadSound("normal/3f.mp3"))
        loaded.value!!.add(loadSound("normal/3fd.mp3"))
        loaded.value!!.add(loadSound("normal/3g.mp3"))
        loaded.value!!.add(loadSound("normal/3gd.mp3"))
        loaded.value!!.add(loadSound("normal/3a.mp3"))
        loaded.value!!.add(loadSound("normal/3ad.mp3"))
        loaded.value!!.add(loadSound("normal/3b.mp3"))

        loaded.value!!.add(loadSound("normal/4c.mp3"))
        loaded.value!!.add(loadSound("normal/4cd.mp3"))
        loaded.value!!.add(loadSound("normal/4d.mp3"))
        loaded.value!!.add(loadSound("normal/4dd.mp3"))
        loaded.value!!.add(loadSound("normal/4e.mp3"))
        loaded.value!!.add(loadSound("normal/4f.mp3"))
        loaded.value!!.add(loadSound("normal/4fd.mp3"))
        loaded.value!!.add(loadSound("normal/4g.mp3"))
        loaded.value!!.add(loadSound("normal/4gd.mp3"))
        loaded.value!!.add(loadSound("normal/4a.mp3"))
        loaded.value!!.add(loadSound("normal/4ad.mp3"))
        loaded.value!!.add(loadSound("normal/4b.mp3"))

        loaded.value!!.add(loadSound("normal/5c.mp3"))
        loaded.value!!.add(loadSound("normal/5cd.mp3"))
        loaded.value!!.add(loadSound("normal/5d.mp3"))
        loaded.value!!.add(loadSound("normal/5dd.mp3"))
        loaded.value!!.add(loadSound("normal/5e.mp3"))
        loaded.value!!.add(loadSound("normal/5f.mp3"))
        loaded.value!!.add(loadSound("normal/5fd.mp3"))
        loaded.value!!.add(loadSound("normal/5g.mp3"))
        loaded.value!!.add(loadSound("normal/5gd.mp3"))
        loaded.value!!.add(loadSound("normal/5a.mp3"))
        loaded.value!!.add(loadSound("normal/5ad.mp3"))
        loaded.value!!.add(loadSound("normal/5b.mp3"))

        loaded.value!!.add(loadSound("normal/6c.mp3"))
        loaded.value!!.add(loadSound("normal/6cd.mp3"))
        loaded.value!!.add(loadSound("normal/6d.mp3"))
        loaded.value!!.add(loadSound("normal/6dd.mp3"))
        loaded.value!!.add(loadSound("normal/6e.mp3"))
        loaded.value!!.add(loadSound("normal/6f.mp3"))
        loaded.value!!.add(loadSound("normal/6fd.mp3"))
        loaded.value!!.add(loadSound("normal/6g.mp3"))
        loaded.value!!.add(loadSound("normal/6gd.mp3"))
        loaded.value!!.add(loadSound("normal/6a.mp3"))
        loaded.value!!.add(loadSound("normal/6ad.mp3"))
        loaded.value!!.add(loadSound("normal/6b.mp3"))

        loaded.value!!.add(loadSound("normal/7c.mp3"))
        loaded.value!!.add(loadSound("normal/7cd.mp3"))
        loaded.value!!.add(loadSound("normal/7d.mp3"))
        loaded.value!!.add(loadSound("normal/7dd.mp3"))
        loaded.value!!.add(loadSound("normal/7e.mp3"))


        loadedHarmonics.value!!.add(loadSound("harmonics/3e.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/3f.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/3fd.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/3g.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/3gd.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/3a.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/3ad.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/3b.mp3"))

        loadedHarmonics.value!!.add(loadSound("harmonics/4c.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/4cd.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/4d.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/4dd.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/4e.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/4f.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/4fd.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/4g.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/4gd.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/4a.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/4ad.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/4b.mp3"))

        loadedHarmonics.value!!.add(loadSound("harmonics/5c.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/5cd.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/5d.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/5dd.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/5e.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/5f.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/5fd.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/5g.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/5gd.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/5a.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/5ad.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/5b.mp3"))

        loadedHarmonics.value!!.add(loadSound("harmonics/6c.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/6cd.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/6d.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/6dd.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/6e.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/6f.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/6fd.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/6g.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/6gd.mp3"))
        loadedHarmonics.value!!.add(loadSound("harmonics/6a.mp3"))

    }

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

    fun getHarmonic(position: Int, fret: Int): Int =
        getOffset(position) + getHarmonicOffset(fret) - 12


    private fun getHarmonicOffset(fret: Int): Int =
        when (fret) {
            12 -> 12
            7, 19 -> 12 + 7
            5, 24 -> 24
            4, 9, 16 -> 24 + 4
            else -> -1
        }
}
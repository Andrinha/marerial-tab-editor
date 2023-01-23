package com.example.marerialtabeditor.repository.data.tab

import androidx.room.TypeConverter
import com.example.marerialtabeditor.repository.data.Song
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SongTypeConverter {
    @TypeConverter
    fun saveSong(song: Song): String? {
        return Gson().toJson(song)
    }

    @TypeConverter
    fun getSong(song: String): Song {
        return Gson().fromJson(
            song,
            object : TypeToken<Song>() {}.type
        )
    }
}
package com.example.marerialtabeditor.repository.data.tab

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.marerialtabeditor.repository.data.Song
import java.io.Serializable

@Entity(tableName = "tab_table")
data class Tab (
    @PrimaryKey(autoGenerate = true)
    val tabId: Int,
    @field:TypeConverters(SongTypeConverter::class)
    val song : Song
): Serializable
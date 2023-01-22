package com.example.marerialtabeditor.utils

import com.example.marerialtabeditor.repository.data.Note
import com.example.marerialtabeditor.repository.data.Song

class Hardcoded {
    val songs = listOf(
        Song(mutableListOf(), "BILO IV", "David Maxim Micic"),
        Song(mutableListOf(), "Звезда по имени Солнце", "Кино"),
        Song(mutableListOf(), "Name", "Band"),
        Song(mutableListOf(), "Name", "Band"),
        Song(mutableListOf(), "Name", "Band"),
        Song(mutableListOf(), "Wish You Were Here", "Pink Floyd"),
        Song(mutableListOf(), "Name", "Band"),
    )


    fun note600(): MutableList<Note> {
        val list: MutableList<Note> = mutableListOf()
        repeat(60) {
            list.add(Note(-1))
        }
        return list
    }
}
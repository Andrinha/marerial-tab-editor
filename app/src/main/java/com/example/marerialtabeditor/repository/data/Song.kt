package com.example.marerialtabeditor.repository.data

class Song(val notes: MutableList<Note>, var name: String, var band: String, var bpm: Int) :
    java.io.Serializable {

    fun addEmptyChunk() {
        repeat(6 * 8) {
            notes.add(Note(-1))
        }
    }
}
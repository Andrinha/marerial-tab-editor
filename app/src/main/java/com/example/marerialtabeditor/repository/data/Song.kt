package com.example.marerialtabeditor.repository.data

class Song(val notes: MutableList<Note>, var name: String = "baba", var band: String = "baba") {

    fun addEmptyChunk() {
        repeat(6 * 8) {
            notes.add(Note(-1))
        }
    }

//    fun applyValue(position: Position, note: Note): Song {
//        chunkList[position.chunk].value[position.offset] = note
//        return Song(chunkList, name, band)
//    }
}
package com.example.marerialtabeditor.repository.data

class Note(fret: Int) {
    var type: Flags = Flags.DEFAULT
    var fret: Int = fret
        set(value) {field = if (value < -1) -1 else value}

    enum class Flags {
        DEFAULT, HARMONIC, MUTED
    }

    override fun toString(): String {
        return when (type) {
            Flags.MUTED -> {
                "X"
            }
            Flags.HARMONIC -> {
                if (fret == -1)
                    ""
                else
                    "<$fret>"
            }
            Flags.DEFAULT -> {
                if (fret == -1) {
                    ""
                } else {
                    "$fret"
                }
            }
        }
    }
}
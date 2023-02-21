package com.example.marerialtabeditor.repository.data

class Note(fret: Int) {
    var type: Type = Type.DEFAULT
    var fret: Int = fret
        set(value) {
            field = when {
                value < -1 -> -1
                value > 24 -> 24
                else -> value
            }
        }

    enum class Type {
        DEFAULT, HARMONIC, MUTED
    }

    override fun toString(): String {
        return when (type) {
            Type.MUTED -> {
                "X"
            }
            Type.HARMONIC -> {
                if (fret == -1)
                    ""
                else
                    "<$fret>"
            }
            Type.DEFAULT -> {
                if (fret == -1) {
                    ""
                } else {
                    "$fret"
                }
            }
        }
    }
}
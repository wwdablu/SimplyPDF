package com.wwdablu.soumya.simplypdf.composers.models.cell

import com.wwdablu.soumya.simplypdf.composers.models.TextProperties

class TextCell(val text: String, val properties: TextProperties?, width: Int) : Cell() {
    init {
        this.width = width
    }
}
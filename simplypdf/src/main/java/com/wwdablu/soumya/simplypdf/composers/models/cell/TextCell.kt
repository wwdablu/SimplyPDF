package com.wwdablu.soumya.simplypdf.composers.models.cell

import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.models.TextProperties

class TextCell(val text: String,
               simplyPdfDocument: SimplyPdfDocument,
               val properties: TextProperties,
               private val width: Int)
    : Cell(simplyPdfDocument) {

    override fun getCellHeight(): Int {
        return simplyPdfDocument.text.write(text, properties, width, verticalPadding,
            true, 0, horizontalPadding, false)
    }

    override fun getCellWidth(): Int {
        return width
    }

    override fun render(xTranslate: Int): Boolean {
        return simplyPdfDocument.text.write(text, properties,
            width, verticalPadding,
            true, xTranslate, horizontalPadding, true) != 0
    }
}
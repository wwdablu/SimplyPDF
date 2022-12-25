package com.wwdablu.soumya.simplypdf.composers.properties.cell

import com.wwdablu.soumya.simplypdf.composers.properties.TextProperties

class TextCell(private val text: String,
               private val properties: TextProperties,
               width: Int) : Cell(width) {

    override fun getCellHeight(): Int {

        return if(!isDocumentSet()) 0 else
            simplyPdfDocument.text.write(text, properties, getCellWidth(), xMargin,
                yMargin, 0, cell = this, performDraw = false)
    }

    override fun getCellWidth(): Int = if (!isDocumentSet()) 0
        else if (width == MATCH_PARENT) simplyPdfDocument.usablePageWidth
        else width

    override fun getContentWidth(): Int {
        return simplyPdfDocument.text.write(text, properties, getCellWidth(), xMargin,
            yMargin, 0, cell = this, performDraw = false)
    }

    override fun render(xTranslate: Int): Boolean {
        if(!isDocumentSet()) return false

        return simplyPdfDocument.text.write(text, properties, getCellWidth(), xMargin,
            yMargin, xTranslate, cell = this, performDraw = true) != 0
    }
}
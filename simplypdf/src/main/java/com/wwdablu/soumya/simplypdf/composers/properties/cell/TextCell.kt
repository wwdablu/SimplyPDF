package com.wwdablu.soumya.simplypdf.composers.properties.cell

import com.wwdablu.soumya.simplypdf.composers.properties.TextProperties

class TextCell(private val text: String,
               private val properties: TextProperties,
               width: Int) : Cell(width) {

    override fun getCellHeight(): Int {

        return if(!isDocumentSet()) 0 else
            simplyPdfDocument.text.write(text, properties, width, xMargin,
                yMargin, 0, this, false)
    }

    override fun getCellWidth(): Int = if (!isDocumentSet()) 0 else width

    override fun getContentWidth(): Int {
        return simplyPdfDocument.text.write(text, properties, width, xMargin,
            yMargin, 0, this, false)
    }

    override fun render(xTranslate: Int): Boolean {
        if(!isDocumentSet()) return false
        return simplyPdfDocument.text.write(text, properties, width, xMargin,
            yMargin, xTranslate, this, true) != 0
    }
}